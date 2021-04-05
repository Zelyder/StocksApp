package com.zelyder.stocksapp.domain.models

import kotlinx.serialization.SerialName

data class News(
    val ticker: String = "",
    val publishedDate: String = "",
    val title: String = "",
    val image: String = "",
    val site: String = "",
    val text: String = "",
    val url: String = ""
)
