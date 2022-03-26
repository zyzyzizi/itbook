package com.twobros.itstore.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twobros.itstore.repostory.Repository

class BookDetailViewModelFactory(
    private val application: Application,
    private val intent: Intent,
    private val bookStoreRepository: Repository

) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Application::class.java,
            Intent::class.java,
            Repository::class.java
        )
            .newInstance(application, intent, bookStoreRepository)
    }
}