package com.twobros.itstore.repostory.api.model

data class SearchResult(
    val books: List<Book>,
    val error: String,
    val page: String,
    val total: String
)