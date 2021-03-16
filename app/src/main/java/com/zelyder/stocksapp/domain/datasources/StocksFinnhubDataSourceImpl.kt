package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.FINNHUB_API_KEY
import com.zelyder.stocksapp.data.network.apis.FinnhubApi
import com.zelyder.stocksapp.data.network.dto.finnhub.SearchResultDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StocksFinnhubDataSourceImpl(private val finnhubApi: FinnhubApi): StocksFinnhubDataSource {
    override suspend fun searchStock(query: String): SearchResultDto = withContext(Dispatchers.IO){
        finnhubApi.searchStock(FINNHUB_API_KEY, query)
    }
}