package com.twobros.itstore.repostory.api.model

interface  IBook

data class Book(
    val image: String,
    val isbn13: String,
    val price: String,
    val subtitle: String,
    val title: String,
    val url: String,
) : IBook