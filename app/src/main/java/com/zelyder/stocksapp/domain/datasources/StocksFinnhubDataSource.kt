package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.network.dto.finnhub.SearchResultDto

interface StocksFinnhubDataSource {
    suspend fun searchStock(query: String): SearchResultDto
}