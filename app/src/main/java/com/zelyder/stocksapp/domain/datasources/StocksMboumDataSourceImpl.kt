package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.network.apis.MboumApi
import com.zelyder.stocksapp.data.network.dto.mboum.MostActivesStocksDto
import com.zelyder.stocksapp.data.network.dto.mboum.TrendingStocksDto

class StocksMboumDataSourceImpl(private val mboumApi: MboumApi) : StocksMboumDataSource {
    override suspend fun getTrendingStocks(): TrendingStocksDto =
        mboumApi.getTrendingStocks()[0]

    override suspend fun getMostActivesStocks(): MostActivesStocksDto =
        mboumApi.getMostActivesStocks()
}