package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.data.mappers.toStockCandle
import com.zelyder.stocksapp.data.network.SocketUpdate
import com.zelyder.stocksapp.data.network.WebServicesProvider
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.models.SelectedItem
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


    override suspend fun getStockCandles(ticker: String, selectedItem: SelectedItem): StockCandle =
        withContext(Dispatchers.IO) {

            val resolution: String
            val fromTimestamp: Long
            val toTimestamp: Long = System.currentTimeMillis()

            when (selectedItem) {
                SelectedItem.DAY -> {
                    resolution = "5"
                    fromTimestamp = toTimestamp - DAY_IN_MILLIS * 3
                }
                SelectedItem.WEEK -> {
                    resolution = "60"
                    fromTimestamp = toTimestamp - DAY_IN_MILLIS * 14
                }
                SelectedItem.MONTH -> {
                    resolution = "D"
                    fromTimestamp = toTimestamp - DAY_IN_MILLIS * 60
                }
                SelectedItem.SIX_MONTHS -> {
                    resolution = "D"
                    fromTimestamp = toTimestamp - DAY_IN_MILLIS * 182
                }
                SelectedItem.YEAR -> {
                    resolution = "D"
                    fromTimestamp = toTimestamp - DAY_IN_MILLIS * 365
                }
            }


            finnhubDataSource.getStockCandles(
                ticker,
                resolution,
                fromTimestamp / 1000,
                toTimestamp / 1000
            ).toStockCandle()
        }

    @ExperimentalCoroutinesApi
    override suspend fun closeSocket() = withContext(Dispatchers.IO) {
        webServicesProvider.stopSocket()
    }

    @ExperimentalCoroutinesApi
    override suspend fun startSocket(ticker: String): Channel<SocketUpdate> =
        withContext(Dispatchers.IO) {
            webServicesProvider.startSocket(ticker)
        }


}

const val DAY_IN_MILLIS: Long = 86400000