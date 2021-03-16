package com.zelyder.stocksapp.data.network.dto.finnhub

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoundStockDto (
    @SerialName("description")
    val description: String,
    @SerialName("displaySymbol")
    val displaySymbol: String,
    @SerialName("symbol")
    val symbol: String,
    @SerialName("type")
    val type: String?
)