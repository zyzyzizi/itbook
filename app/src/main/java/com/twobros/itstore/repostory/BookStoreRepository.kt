package com.twobros.itstore.repostory

import com.twobros.itstore.repostory.api.BookStoreApiProvider
import com.twobros.itstore.repostory.api.model.BookInfo
import com.twobros.itstore.repostory.api.model.SearchResult
import io.reactivex.rxjava3.core.Single

class BookStoreRepository : Repository {
    private val storeApi = BookStoreApiProvider.bookStoreRxApi

    override fun search(query: String): Single<SearchResult> {
        return storeApi.search(query)
    }

    override fun search(query: String, page: Int): Single<SearchResult> {
        return storeApi.search(query, page)
    }

    override fun requestDetail(isbn13: String): Single<BookInfo> {
        return storeApi.request(isbn13)
    }
}