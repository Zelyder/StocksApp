package com.zelyder.stocksapp.data.network.dto.mboum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MostActivesStockDto(
    @SerialName("currency")
    val currency: String = "",
    @SerialName("regularMarketPrice")
    val regularMarketPrice: Float = 0.0f,
    @SerialName("regularMarketChange")
    val regularMarketChange: Float = 0.0f,
    @SerialName("regularMarketChangePercent")
    val regularMarketChangePercent: Float = 0.0f,
    @SerialName("shortName")
    val shortName: String = "",
    @SerialName("symbol")
    val ticker: String = ""

)