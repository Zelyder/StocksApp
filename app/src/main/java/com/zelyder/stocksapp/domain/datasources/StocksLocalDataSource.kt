package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.storage.entities.StockEntity

interface StocksLocalDataSource {
    suspend fun getStocksAsync(): List<StockEntity>
    suspend fun getStockByTicker(ticker: String)
    suspend fun saveStocks(stocks: List<StockEntity>)
    suspend fun saveStock(stock: StockEntity)
    suspend fun updateStock(stock: StockEntity)
    suspend fun updateStockIsFavorite(ticker: String, isFavorite: Boolean)
    suspend fun deleteStockByTicker(ticker: String)
    suspend fun deleteAllStocks()

}