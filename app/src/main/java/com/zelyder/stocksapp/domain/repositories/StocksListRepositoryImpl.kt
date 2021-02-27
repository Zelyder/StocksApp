package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.data.LOGO_BASE_URL
import com.zelyder.stocksapp.domain.datasources.StocksRemoteDataSource
import com.zelyder.stocksapp.domain.models.Stock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.round

class StocksListRepositoryImpl(
    private val remoteDataSource: StocksRemoteDataSource
): StocksListRepository {
    override suspend fun getStocksAsync(forceRefresh: Boolean): List<Stock> = withContext(Dispatchers.IO) {
        val stocks = remoteDataSource.getMostActivesStocks().stocks.map { Stock(
            ticker = it.ticker,
            logo = "$LOGO_BASE_URL${it.ticker}",
            companyName = it.shortName,
            price = it.regularMarketPrice,
            currency = it.currency,
            dayDelta =  round(it.postMarketPrice*it.regularMarketChangePercent)/100.0f  //round((it.postMarketPrice - it.regularMarketPrice) * 100.0f) / 100.0f
        ) }
        stocks
    }
}