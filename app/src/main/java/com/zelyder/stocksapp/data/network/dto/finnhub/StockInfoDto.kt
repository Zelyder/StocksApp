package com.zelyder.stocksapp.data.network.dto.finnhub

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockInfoDto (
    @SerialName("country")
    val country: String = "",
    @SerialName("currency")
    val currency: String = "",
    @SerialName("exchange")
    val exchange: String = "",
    @SerialName("ipo")
    val ipo: String = "",
    @SerialName("marketCapitalization")
    val marketCapitalization: Long = 0L,
    @SerialName("name")
    val name: String = "",
    @SerialName("phone")
    val phone: String = "",
    @SerialName("shareOutstanding")
    val shareOutstanding: Double = 0.0,
    @SerialName("ticker")
    val ticker: String = "",
    @SerialName("weburl")
    val weburl: String = "",
    @SerialName("logo")
    val logo: String = "",
    @SerialName("finnhubIndustry")
    val finnhubIndustry: String = ""
)