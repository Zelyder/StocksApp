package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.network.dto.finnhub.*

interface StocksFinnhubDataSource {
    suspend fun searchStock(query: String): SearchResultDto
    suspend fun getInfoByTicker(ticker: String): StockInfoDto
    suspend fun getPriceByTicker(ticker: String): StockPriceDto
    suspend fun getStockCandles(
        ticker: String,
        resolution: String,
        fromTimestamp: Long,
        toTimestamp: Long,
    ): StockCandlesDto
    suspend fun getIndexConstituents(): IndexConstituentsDto
}