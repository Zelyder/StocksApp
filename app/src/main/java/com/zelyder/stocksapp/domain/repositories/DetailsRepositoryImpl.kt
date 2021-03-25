package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.data.mappers.toStockCandle
import com.zelyder.stocksapp.data.network.SocketUpdate
import com.zelyder.stocksapp.data.network.WebServicesProvider
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.models.StockCandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import okhttp3.WebSocket
import java.sql.Timestamp

class DetailsRepositoryImpl @ExperimentalCoroutinesApi constructor(
    private val finnhubDataSource: StocksFinnhubDataSource,
    private val localDataSource: StocksLocalDataSource,
    private val webServicesProvider: WebServicesProvider
) : DetailsRepository {



    override suspend fun getStockCandles(ticker: String): StockCandle = withContext(Dispatchers.IO){
        finnhubDataSource.getStockCandles(
            ticker,
            "D",
            (System.currentTimeMillis() - _30_DAYS_IN_MILLIS)/1000,
            System.currentTimeMillis()/1000
        ).toStockCandle()
    }

    @ExperimentalCoroutinesApi
    override suspend fun closeSocket() = withContext(Dispatchers.IO){
        webServicesProvider.stopSocket()
    }

    @ExperimentalCoroutinesApi
    override suspend fun startSocket(ticker: String): Channel<SocketUpdate> = withContext(Dispatchers.IO){
        webServicesProvider.startSocket(ticker)
    }


}

const val _30_DAYS_IN_MILLIS: Long = 2592000000