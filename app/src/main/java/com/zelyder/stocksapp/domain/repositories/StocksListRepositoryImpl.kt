package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.data.DataSource
import com.zelyder.stocksapp.data.LOGO_BASE_URL
import com.zelyder.stocksapp.data.mappers.toEntity
import com.zelyder.stocksapp.data.mappers.toStock
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.datasources.StocksRemoteDataSource
import com.zelyder.stocksapp.domain.models.Stock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.round

class StocksListRepositoryImpl(
        private val remoteDataSource: StocksRemoteDataSource,
        private val localDataSource: StocksLocalDataSource
) : StocksListRepository {
    override suspend fun getStocksAsync(forceRefresh: Boolean): List<Stock> = withContext(Dispatchers.IO) {



        var stocks = localDataSource.getStocksAsync().map { it.toStock() }

        if(forceRefresh || stocks.isEmpty()) {
            stocks = remoteDataSource.getMostActivesStocks().stocks.map { it.toStock() }
            localDataSource.saveStocks(stocks.map { it.toEntity() })
        }

        stocks
//        DataSource.getStocks()
    }
}