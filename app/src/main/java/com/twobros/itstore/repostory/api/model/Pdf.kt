package com.twobros.itstore.repostory.api.model

import com.google.gson.annotations.SerializedName

data class Pdf(
    @SerializedName("Chapter 2") val Chapter_2: String,
    @SerializedName("Chapter 5") val Chapter_5: String
)