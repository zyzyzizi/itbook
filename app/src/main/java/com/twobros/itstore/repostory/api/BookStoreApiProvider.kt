package com.twobros.itstore.repostory.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


private const val API_URL = "https://api.itbook.store/"

val bookStoreApi: BookStoreApi = Retrofit.Builder().apply {
    baseUrl(API_URL)
    addConverterFactory(GsonConverterFactory.create())
    addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
    val httpClient = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }.build()
    client(httpClient)
}.build().create(BookStoreApi::class.java)