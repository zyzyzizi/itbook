package com.twobros.itstore.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.twobros.itstore.Queries
import com.twobros.itstore.repostory.Repository
import com.twobros.itstore.repostory.api.model.Book
import com.twobros.itstore.util.isNetworkAvailable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchViewModel(
    application: Application, private val bookStoreRepository: Repository
) : AndroidViewModel(application) {
    companion object {
        private val TAG = "shk-${SearchViewModel::class.java.simpleName}"
        const val NUM_ITEM_IN_PAGE = 10
    }

    @VisibleForTesting
    val bookList = ArrayList<Book>()
    val bookLiveData = MutableLiveData<List<Book>>()
    val isLoading = MutableLiveData(false)
    val errorMessage = MutableLiveData<String?>()
    private val disposables = CompositeDisposable()

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    data class Query(val query: String) {
        var isQueried: Boolean = false
        var total: Int = 0
        var totalPages: Int = 0

        var loadedPages: Int = 0

        fun isFullLoaded(): Boolean {
            return loadedPages != 0 && totalPages == loadedPages
        }

        override fun equals(other: Any?): Boolean {
            return (other is Query) && this.query.equals(other.query, ignoreCase = true)
        }

        override fun hashCode(): Int {
            return query.hashCode()
        }
    }

    private var queIdx = 0
    private val queryQue = ArrayList<Query>()
    private var queries: Queries? = null

    fun init(qu: Queries) {
        bookList.clear()
        queryQue.clear()
        queIdx = 0

        queries = qu

        queryQue.add(Query(qu.query1!!))
        qu.query2?.let {
            queryQue.add(Query(it))
        }
    }

    fun load(): Boolean {
        if (!isNetworkAvailable(context)) {
            errorMessage.postValue("Network is not available")
            return false
        }
        val currentQuery = queryQue[queIdx]

        if (currentQuery.loadedPages != 0 && currentQuery.isFullLoaded()) {
            Log.i(TAG, "load: full loaded")
            return false
        }

        isLoading.postValue(true)

        disposables.add(
            bookStoreRepository.search(currentQuery.query, currentQuery.loadedPages + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        isLoading.value = false
                        if (!currentQuery.isQueried) {
                            currentQuery.isQueried = true
                            currentQuery.total = response.total.toInt()
                            currentQuery.totalPages = currentQuery.total / NUM_ITEM_IN_PAGE + 1
                        }

                        currentQuery.loadedPages = response.page.toInt()


                        if (currentQuery.total == 0) {
                            errorMessage.value = "Result: no data"
                        } else {
                            errorMessage.value = null
                            updateResultList(response.books)
                            if (currentQuery.isFullLoaded() && queries?.op == Queries.OP.OR) {
                                queIdx = 1
                                load()
                            }
                        }
                    },
                    { ex ->
                        Log.e(TAG, "load: onError: $ex | ${ex.message}")
                        isLoading.value = false
                        errorMessage.value = "Error (${ex.message})"
                    }
                ))

        return true
    }

    @VisibleForTesting
    @MainThread
    fun updateResultList(books: List<Book>) {
        books.filter {
            var exclude = false
            if (queries != null && queries!!.op == Queries.OP.NOT) {
                exclude = it.title.contains(queries!!.query2!!, ignoreCase = true)
            }
            !bookList.contains(it) && !exclude
        }.let { filteredList ->
            bookList.addAll(filteredList)
            bookLiveData.value = filteredList
        }
        Log.i(TAG, "updateResultList")
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}