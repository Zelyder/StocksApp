package com.zelyder.stocksapp.data.network.dto.fmp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NasdaqConstituentDto(
    @SerialName("symbol")
    val symbol: String,
    @SerialName("name")
    val name: String,
    @SerialName("sector")
    val sector: String,
    @SerialName("subSector")
    val subSector: String,
    @SerialName("headQuarter")
    val headQuarter: String? = null,
    @SerialName("dateFirstAdded")
    val dateFirstAdded: String,
    @SerialName("cik")
    val cik: String? = null,
    @SerialName("founded")
    val founded: String? = null
)
