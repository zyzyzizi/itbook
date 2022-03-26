package com.twobros.itstore.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.twobros.itstore.repostory.Repository
import com.twobros.itstore.repostory.api.model.BookInfo
import com.twobros.itstore.util.isNetworkAvailable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@SuppressLint("StaticFieldLeak")
class BookDetailViewModel(
    application: Application,
    launchIntent: Intent,
    private val bookStoreRepository: Repository
) : AndroidViewModel(application) {
    companion object {
        const val KEY_ISBN = "isbn"
        private val TAG = "shk-${BookDetailViewModel::class.java.simpleName}"
    }

    private val isbn13: String? = launchIntent.getStringExtra(KEY_ISBN)
    private val disposables: CompositeDisposable = CompositeDisposable()
    val isLoading = MutableLiveData(false)
    val errorMessage = MutableLiveData<String?>()
    val bookInfoLiveData = MutableLiveData<BookInfo>()
    private val context = application.applicationContext

    @MainThread
    fun load() {
        if (!isNetworkAvailable(context)) {
            errorMessage.postValue("Network is not available")
            return
        }
        disposables.add(
            bookStoreRepository
                .requestDetail(isbn13!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        isLoading.value = false
                        errorMessage.value = null
                        bookInfoLiveData.value = response
                    },
                    { ex ->
                        Log.e(TAG, "load: onError: $ex | ${ex.message}")
                        isLoading.value = false
                        errorMessage.value = "Error (${ex.message}"
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
