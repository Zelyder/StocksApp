package com.zelyder.stocksapp.data.network.dto.finnhub

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultDto (
    @SerialName("count")
    val count: Long = 0L,
    @SerialName("result")
    val foundStocks: List<FoundStockDto> = listOf()
)
