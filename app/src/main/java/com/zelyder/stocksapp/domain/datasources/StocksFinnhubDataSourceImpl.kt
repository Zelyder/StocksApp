package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.FINNHUB_API_KEY
import com.zelyder.stocksapp.data.network.apis.FinnhubApi
import com.zelyder.stocksapp.data.network.dto.finnhub.SearchResultDto
import com.zelyder.stocksapp.data.network.dto.finnhub.StockCandlesDto
import com.zelyder.stocksapp.data.network.dto.finnhub.StockInfoDto
import com.zelyder.stocksapp.data.network.dto.finnhub.StockPriceDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.Query

class StocksFinnhubDataSourceImpl(private val finnhubApi: FinnhubApi) : StocksFinnhubDataSource {
    override suspend fun searchStock(query: String): SearchResultDto = withContext(Dispatchers.IO) {
        finnhubApi.searchStock(FINNHUB_API_KEY, query)
    }

    override suspend fun getInfoByTicker(ticker: String): StockInfoDto =
        withContext(Dispatchers.IO) {
            finnhubApi.getInfoByTicker(FINNHUB_API_KEY, ticker)
        }

    override suspend fun getPriceByTicker(ticker: String): StockPriceDto =
        withContext(Dispatchers.IO) {
            finnhubApi.getPriceByTicker(FINNHUB_API_KEY, ticker)
        }

    override suspend fun getStockCandles(
        ticker: String, resolution: String,
        fromTimestamp: Long,
        toTimestamp: Long,
    ): StockCandlesDto  = withContext(Dispatchers.IO){
        finnhubApi.getStockCandles(FINNHUB_API_KEY, ticker, resolution, fromTimestamp, toTimestamp)
    }
}