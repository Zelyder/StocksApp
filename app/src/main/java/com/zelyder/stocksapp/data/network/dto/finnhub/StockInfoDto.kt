package com.zelyder.stocksapp.data.network.dto.finnhub

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockInfoDto (
    @SerialName("country")
    val country: String? = null,
    @SerialName("currency")
    val currency: String? = null,
    @SerialName("exchange")
    val exchange: String? = null,
    @SerialName("ipo")
    val ipo: String? = null,
    @SerialName("marketCapitalization")
    val marketCapitalization: Long? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("shareOutstanding")
    val shareOutstanding: Double? = null,
    @SerialName("ticker")
    val ticker: String? = null,
    @SerialName("weburl")
    val weburl: String? = null,
    @SerialName("logo")
    val logo: String? = null,
    @SerialName("finnhubIndustry")
    val finnhubIndustry: String? = null
)