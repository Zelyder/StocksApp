package com.zelyder.stocksapp.data.network.dto.mboum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MostActivesStocksDto(
    @SerialName("start")
    val start: Int = 0,
    @SerialName("count")
    val count: Int = 0,
    @SerialName("total")
    val total: Int = 0,
    @SerialName("description")
    val description: String = "",
    @SerialName("quotes")
    val stocks: List<MostActivesStockDto> = listOf(),
)