package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.data.LOGO_BASE_URL
import com.zelyder.stocksapp.data.mappers.toEntity
import com.zelyder.stocksapp.data.mappers.toFavoriteStock
import com.zelyder.stocksapp.data.mappers.toStock
import com.zelyder.stocksapp.data.mappers.toStockEntity
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.datasources.StocksMboumDataSource
import com.zelyder.stocksapp.domain.models.Stock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.round

class StocksListRepositoryImpl(
    private val mboumDataSource: StocksMboumDataSource,
    private val localDataSource: StocksLocalDataSource
) : StocksListRepository {
    override suspend fun getStocksAsync(forceRefresh: Boolean): List<Stock> =
        withContext(Dispatchers.IO) {

            var stocks = localDataSource.getStocksAsync().map { it.toStock() }

            if (forceRefresh || stocks.isEmpty()) {
                stocks = mboumDataSource.getMostActivesStocks().stocks.map { it.toStock() }
                localDataSource.saveStocks(stocks.map { it.toEntity() })
            }

            stocks
//        DataSource.getStocks()
        }

    override suspend fun updateStocksIsFavoriteAsync(stock: Stock) = withContext(Dispatchers.IO) {
        localDataSource.updateStockIsFavorite(stock.ticker, stock.isFavorite)
        if (stock.isFavorite) {
            localDataSource.addFavoriteStock(stock.toFavoriteStock())
        } else {
            localDataSource.deleteFavoriteStockByTicker(stock.ticker)
        }

    }

    override suspend fun getFavoritesAsync(): List<Stock> = withContext(Dispatchers.IO) {
        localDataSource.getFavoritesStocks()?.map { it.toStock() } ?: listOf()
    }

}