package com.zelyder.stocksapp.data.network.dto.finnhub

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LastPriceDto (
    @SerialName("p")
    val lastPrice: Float,
    @SerialName("s")
    val symbol: String,
    @SerialName("t")
    val timestamp: Long,
    @SerialName("v")
    val volume: Float
)
