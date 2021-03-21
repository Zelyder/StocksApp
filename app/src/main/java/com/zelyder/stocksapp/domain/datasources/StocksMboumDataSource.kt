package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.network.dto.mboum.MostActivesStocksDto
import com.zelyder.stocksapp.data.network.dto.mboum.TrendingStocksDto

interface StocksMboumDataSource {
    suspend fun getTrendingStocks(): TrendingStocksDto
    suspend fun getMostActivesStocks(): MostActivesStocksDto
}