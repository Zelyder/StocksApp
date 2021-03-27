package com.zelyder.stocksapp.data.network.dto.finnhub

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IndexConstituentsDto (
    @SerialName("constituents")
    val tickers: List<String>,
    @SerialName("symbol")
    val indexSymbol: String
)

