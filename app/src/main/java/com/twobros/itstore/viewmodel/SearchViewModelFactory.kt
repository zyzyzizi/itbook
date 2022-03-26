package com.twobros.itstore.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twobros.itstore.repostory.Repository

class SearchViewModelFactory(
    private val application: Application,
    private val bookStoreRepository: Repository
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java, Repository::class.java)
            .newInstance(application, bookStoreRepository)
    }
}