package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.data.mappers.toStockCandle
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.models.StockCandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Timestamp

class DetailsRepositoryImpl(
    private val finnhubDataSource: StocksFinnhubDataSource,
    private val localDataSource: StocksLocalDataSource
) : DetailsRepository {
    override suspend fun getStockCandles(ticker: String): StockCandle = withContext(Dispatchers.IO){
        finnhubDataSource.getStockCandles(
            ticker,
            "D",
            (System.currentTimeMillis() - _30_DAYS_IN_MILLIS)/1000,
            System.currentTimeMillis()/1000
        ).toStockCandle()
    }
}

const val _30_DAYS_IN_MILLIS: Long = 2592000000