package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.network.dto.MostActivesStocksDto
import com.zelyder.stocksapp.data.network.dto.TrendingStocksDto

interface StocksRemoteDataSource {
    suspend fun getTrendingStocks(): TrendingStocksDto
    suspend fun getMostActivesStocks(): MostActivesStocksDto
}