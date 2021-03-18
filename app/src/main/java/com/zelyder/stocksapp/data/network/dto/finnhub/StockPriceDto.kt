package com.zelyder.stocksapp.data.network.dto.finnhub

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockPriceDto (
    @SerialName("c")
    val current: Float = 0.0f,
    @SerialName("h")
    val highDay: Float = 0.0f,
    @SerialName("l")
    val lowDay: Float = 0.0f,
    @SerialName("o")
    val todayOpen: Float = 0.0f,
    @SerialName("pc")
    val previousClose: Float = 0.0f,
    @SerialName("t")
    val t: Long = 0L
)