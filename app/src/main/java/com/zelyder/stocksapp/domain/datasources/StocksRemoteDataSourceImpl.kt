package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.network.apis.MboumApi
import com.zelyder.stocksapp.data.network.dto.MostActivesStocksDto
import com.zelyder.stocksapp.data.network.dto.TrendingStocksDto

class StocksRemoteDataSourceImpl(private val mboumApi: MboumApi) : StocksRemoteDataSource {
    override suspend fun getTrendingStocks(): TrendingStocksDto =
        mboumApi.getTrendingStocks()[0]

    override suspend fun getMostActivesStocks(): MostActivesStocksDto =
        mboumApi.getMostActivesStocks()
}