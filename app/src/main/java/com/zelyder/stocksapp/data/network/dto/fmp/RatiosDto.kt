package com.zelyder.stocksapp.data.network.dto.fmp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatiosDto (
    @SerialName("currentRatioTTM")
    val currentRatio: Double? = 0.0,
    @SerialName("peRatioTTM")
    val peRatio: Double? = 0.0,
    @SerialName("priceToSalesRatioTTM")
    val priceToSalesRatio: Double? = 0.0,
    @SerialName("priceBookValueRatioTTM")
    val priceBookValueRatio: Double? = 0.0,
    @SerialName("dividendPerShareTTM")
    val dividendPerShare: Double? = 0.0,
    @SerialName("freeCashFlowPerShareTTM")
    val freeCashFlowPerShare: Double? = 0.0
)

