package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.network.dto.fmp.NasdaqConstituentDto

interface StocksFmpDataSource {
    suspend fun getNasdaqConstituent(): List<NasdaqConstituentDto>
}