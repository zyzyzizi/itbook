package com.twobros.itstore.viewmodel

import android.content.Intent
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twobros.itstore.repostory.BookStoreRepository
import com.twobros.itstore.repostory.api.model.BookInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class BookDetailViewModel(
    launchIntent: Intent,
    private val bookStoreRepository: BookStoreRepository
) : ViewModel() {
    companion object {
        const val KEY_ISBN = "isbn"
        private val TAG = "shk-${BookDetailViewModel::class.java.simpleName}"
    }

    private val isbn13: String? = launchIntent.getStringExtra(KEY_ISBN)
    private val disposables: CompositeDisposable = CompositeDisposable()
    val isLoading = MutableLiveData(false)
    val errorMessage = MutableLiveData<String?>()
    val bookInfoLiveData = MutableLiveData<BookInfo>()

    @MainThread
    fun load() {
        isLoading.value = true
        disposables.add(
            bookStoreRepository
                .requestBookDetail(isbn13!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        isLoading.value = false
                        if (response.isSuccessful && response.body() != null) {
                            errorMessage.value = null
                            bookInfoLiveData.value = response.body()
                        } else {
                            Log.e(
                                TAG,
                                "load: onSuccess but failed: ${response.code()} | ${response.message()} "
                            )
                            errorMessage.value = "error (${response.code()})"
                        }
                    },
                    { ex ->
                        Log.e(TAG, "load: onError: $ex | ${ex.message}")
                        isLoading.value = false
                        errorMessage.value = "error (${ex.message}"
                    }
                )

        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
