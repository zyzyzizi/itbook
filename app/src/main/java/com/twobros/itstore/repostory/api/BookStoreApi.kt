package com.twobros.itstore.repostory.api

import com.twobros.itstore.repostory.api.model.BookInfo
import com.twobros.itstore.repostory.api.model.SearchResult
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface BookStoreApi {

    @GET("1.0/search/{query}")
    @Headers("Content-Type:application/json")
    fun search(@Path("query") query: String): Single<SearchResult>


    @GET("1.0/search/{query}/{page}")
    @Headers("Content-Type:application/json")
    fun search(
        @Path("query") query: String,
        @Path("page") page: Int
    ): Single<SearchResult>


    @GET("1.0/books/{isbn13}")
    @Headers("Content-Type:application/json")
    fun request(@Path("isbn13") isbn: String): Single<BookInfo>
}