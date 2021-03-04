package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.storage.db.StocksDb
import com.zelyder.stocksapp.data.storage.entities.StockEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StocksLocalDataSourceImpl(private val stocksDb: StocksDb): StocksLocalDataSource {
    override suspend fun getStocksAsync(): List<StockEntity> = withContext(Dispatchers.IO){
        stocksDb.stocksDao().getAllStocks()
    }

    override suspend fun getStockByTicker(ticker: String) {
        stocksDb.stocksDao().getStockByTicker(ticker)
    }

    override suspend fun saveStocks(stocks: List<StockEntity>) = withContext(Dispatchers.IO){
        stocksDb.stocksDao().addAllStocks(stocks)
    }

    override suspend fun saveStock(stock: StockEntity) = withContext(Dispatchers.IO){
        stocksDb.stocksDao().addStock(stock)
    }

    override suspend fun updateStock(stock: StockEntity) = withContext(Dispatchers.IO){
        stocksDb.stocksDao().updateStock(stock)
    }

    override suspend fun updateStockIsFavorite(ticker: String, isFavorite: Boolean) = withContext(Dispatchers.IO){
        stocksDb.stocksDao().updateIsFavoriteByTicker(ticker, isFavorite)
    }

    override suspend fun deleteStockByTicker(ticker: String) = withContext(Dispatchers.IO){
        stocksDb.stocksDao().deleteByTicker(ticker)
    }

    override suspend fun deleteAllStocks() = withContext(Dispatchers.IO){
        stocksDb.stocksDao().deleteAll()
    }
}