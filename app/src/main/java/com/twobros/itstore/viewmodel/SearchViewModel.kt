package com.twobros.itstore.viewmodel

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twobros.itstore.repostory.api.bookStoreApi
import com.twobros.itstore.repostory.api.model.Book
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchViewModel() : ViewModel() {
    private val bookList = ArrayList<Book>()
    val bookLiveData = MutableLiveData<List<Book>>()
    val isLoading = MutableLiveData(false)
    val isError = MutableLiveData<Boolean>()
    private val disposables = CompositeDisposable()

    data class Query(val query: String) {
        var totalPages: Int = 0
        var loadedPages: Int = 0
        override fun equals(other: Any?): Boolean {
            return (other is Query) && this.query.equals(other.query, ignoreCase = true)
        }

        override fun hashCode(): Int {
            return query.hashCode()
        }
    }

    sealed class OP {
        object NA : OP()
        object OR : OP()
        object NOT : OP()
    }

    private var queIdx = 0
    private val queryQue = ArrayList<Query>()
    private var oper: OP? = null

    fun init(q1: String, q2: String? = null, op: OP? = OP.NA) {
        queryQue.clear()
        oper = null
        queIdx = 0

        queryQue.add(Query(q1))
        q2?.let {
            if (q1 != q2) {
                queryQue.add(Query(it))
            }
        }
        if (queryQue.size == 2) {
            oper = op
        }
    }

    fun load(): Boolean {
        val currentQuery = queryQue[queIdx]
        isLoading.postValue(true)

        disposables.add(
            bookStoreApi.search(currentQuery.query, currentQuery.loadedPages + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        isLoading.value = false
                        if (response.isSuccessful && response.body() != null) {
                            currentQuery.totalPages = response.body()?.total?.toInt() ?: 0
                            //todo 0 일때 검색 결과 없음 표기
                            currentQuery.loadedPages = response.body()?.page?.toInt() ?: 0
                            isError.value = false
                            updateResultList(response.body()!!.books)
                        } else {
                            Log.e(
                                TAG,
                                "load: onSuccess but failed: ${response.code()} | ${response.message()}"
                            )
                            isError.value= true
                        }
                    },
                    { ex ->
                        Log.e(TAG, "load: onError: $ex | ${ex.message}")
                        isLoading.value = false
                        isError.value = true
                    }
                ))

        return true
    }

    @MainThread
    private fun updateResultList(books: List<Book>) {
        //todo operation filter
        books.filter { !bookList.contains(it) }.let { filteredList ->
//            bookList.addAll(filteredList)
            bookLiveData.value = filteredList
        }
        Log.i(TAG, "updateResultList")
//        bookLiveData.postValue(bookList)
    }


    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    companion object {
        private val TAG = "shk-${SearchViewModel::class.java.simpleName}"
    }
}