package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.data.mappers.toRatios
import com.zelyder.stocksapp.data.mappers.toStockCandle
import com.zelyder.stocksapp.data.network.SocketUpdate
import com.zelyder.stocksapp.data.network.WebServicesProvider
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksFmpDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.enums.SelectedItem
import com.zelyder.stocksapp.domain.models.Ratio
import com.zelyder.stocksapp.domain.models.StockCandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

class DetailsRepositoryImpl @ExperimentalCoroutinesApi constructor(
    private val finnhubDataSource: StocksFinnhubDataSource,
    private val fmpDataSource: StocksFmpDataSource,
    private val webServicesProvider: WebServicesProvider
) : DetailsRepository {


    override suspend fun getStockCandles(ticker: String, selectedItem: SelectedItem): StockCandle =
        withContext(Dispatchers.IO) {

            val resolution: String
            val fromTimestamp: Long
            val toTimestamp: Long = System.currentTimeMillis()

            // Assigning chart scaling parameters
            when (selectedItem) {
                SelectedItem.DAY -> {
                    resolution = "5"
                    fromTimestamp = toTimestamp - DAY_IN_MILLIS
                }
                SelectedItem.WEEK -> {
                    resolution = "60"
                    fromTimestamp = toTimestamp - DAY_IN_MILLIS * 7
                }
                SelectedItem.MONTH -> {
                    resolution = "D"
                    fromTimestamp = toTimestamp - DAY_IN_MILLIS * 30
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

    override suspend fun getRatios(ticker: String): List<Ratio> = withContext(Dispatchers.IO){
        fmpDataSource.getRatios(ticker)[0].toRatios()
    }


}

const val DAY_IN_MILLIS: Long = 86400000