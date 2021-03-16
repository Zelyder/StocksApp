package com.zelyder.stocksapp.data.network.dto.mboum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray


@Serializable
data class TrendingStocksDto (
    @SerialName("count")
    val count: Long = 0,
    @SerialName("quotes")
    val tickers: List<String> = listOf(),
    @SerialName("jobTimestamp")
    val jobTimestamp: Long = 0,
    @SerialName("startInterval")
    val startInterval: Long  = 0
)
