package com.zelyder.stocksapp.data.network.dto.finnhub

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockCandlesDto (
    @SerialName("c")
    val closePrices: List<Float>,
    @SerialName("h")
    val highPrices: List<Float>,
    @SerialName("l")
    val lowPrices: List<Float>,
    @SerialName("o")
    val openPrices: List<Float>,
    @SerialName("s")
    val statusOfResponse: String,
    @SerialName("t")
    val timeStamps: List<Long>,
    @SerialName("v")
    val volumeData: List<Long>
)