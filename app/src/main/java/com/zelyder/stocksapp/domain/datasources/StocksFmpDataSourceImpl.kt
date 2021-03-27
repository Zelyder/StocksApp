package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.FMP_API_KEY
import com.zelyder.stocksapp.data.network.apis.FmpApi
import com.zelyder.stocksapp.data.network.dto.fmp.NasdaqConstituentDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StocksFmpDataSourceImpl(private val fmpApi: FmpApi): StocksFmpDataSource {
    override suspend fun getNasdaqConstituent(): List<NasdaqConstituentDto> = withContext(Dispatchers.IO){
        fmpApi.getNasdaqConstituent(FMP_API_KEY)
    }
}