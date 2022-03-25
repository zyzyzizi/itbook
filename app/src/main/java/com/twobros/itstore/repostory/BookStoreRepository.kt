package com.twobros.itstore.repostory

import com.twobros.itstore.repostory.api.BookStoreApiProvider

class BookStoreRepository {
    private val storeApi = BookStoreApiProvider.bookStoreRxApi

    fun searchBook(bookTitle: String) = storeApi.search(bookTitle)

    fun searchBook(bookTitle: String, page: Int) = storeApi.search(bookTitle, page)

    fun requestBookDetail(isbn: String) = storeApi.request(isbn)
}