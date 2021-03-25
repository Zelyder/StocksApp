package com.zelyder.stocksapp.data.network.dto.finnhub

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockLastPriceDto (
    @SerialName("data")
    val data: List<LastPriceDto>,
    @SerialName("type")
    val type: String
)


