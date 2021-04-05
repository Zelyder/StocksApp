package com.zelyder.stocksapp.data.network.dto.fmp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto (
    @SerialName("symbol")
    val symbol: String,
    @SerialName("publishedDate")
    val publishedDate: String,
    @SerialName("title")
    val title: String,
    @SerialName("image")
    val image: String,
    @SerialName("site")
    val site: String,
    @SerialName("text")
    val text: String,
    @SerialName("url")
    val url: String
)
