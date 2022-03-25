package com.twobros.itstore.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twobros.itstore.repostory.BookStoreRepository

class BookDetailViewModelFactory(private val intent: Intent, private val bookStoreRepository: BookStoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Intent::class.java, BookStoreRepository::class.java).newInstance(intent, bookStoreRepository)
    }
}