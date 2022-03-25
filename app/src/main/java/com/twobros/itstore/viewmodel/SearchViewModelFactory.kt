package com.twobros.itstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twobros.itstore.repostory.BookStoreRepository

class SearchViewModelFactory(private val bookStoreRepository: BookStoreRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(BookStoreRepository::class.java).newInstance(bookStoreRepository)
    }
}