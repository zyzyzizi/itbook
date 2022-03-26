package com.twobros.itstore.repostory

import com.twobros.itstore.repostory.api.model.BookInfo
import com.twobros.itstore.repostory.api.model.SearchResult
import io.reactivex.rxjava3.core.Single

interface Repository {
    fun search(query:String) : Single<SearchResult>
    fun search(query:String, page: Int): Single<SearchResult>
    fun requestDetail(isbn13: String) : Single<BookInfo>
}