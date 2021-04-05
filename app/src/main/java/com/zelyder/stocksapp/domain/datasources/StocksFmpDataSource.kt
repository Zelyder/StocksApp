package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.network.dto.fmp.NasdaqConstituentDto
import com.zelyder.stocksapp.data.network.dto.fmp.RatiosDto

interface StocksFmpDataSource {
    suspend fun getNasdaqConstituent(): List<NasdaqConstituentDto>
    suspend fun getRatios(ticker: String): List<RatiosDto>
}