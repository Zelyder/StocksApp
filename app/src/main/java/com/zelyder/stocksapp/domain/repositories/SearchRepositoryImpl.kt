package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.data.mappers.toEntity
import com.zelyder.stocksapp.data.mappers.toFavoriteStock
import com.zelyder.stocksapp.data.mappers.toStock
import com.zelyder.stocksapp.data.storage.entities.PopularQueriesEntity
import com.zelyder.stocksapp.data.storage.entities.RecentQueriesEntity
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.datasources.StocksMboumDataSource
import com.zelyder.stocksapp.domain.models.Stock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    private val mboumDataSource: StocksMboumDataSource,
    private val finnhubDataSource: StocksFinnhubDataSource,
    private val localDataSource: StocksLocalDataSource
) : SearchRepository {
    override suspend fun searchStock(query: String): List<Stock> = withContext(Dispatchers.IO) {
        // Removing regional ticker
        val stocks = finnhubDataSource.searchStock(query).foundStocks.filter { !it.symbol.contains(".") }.map {
            it.toStock(finnhubDataSource.getPriceByTicker(it.symbol),
                localDataSource.getFavoriteStockByTicker(it.symbol) != null
            )
        }
        stocks
    }

    override suspend fun updateStocksIsFavoriteAsync(stock: Stock) =
        withContext(Dispatchers.IO) {
            localDataSource.updateStockIsFavorite(stock.ticker, stock.isFavorite)
            if (stock.isFavorite) {
                localDataSource.addFavoriteStock(stock.toFavoriteStock())
            } else {
                localDataSource.deleteFavoriteStockByTicker(stock.ticker)
            }

        }

    override suspend fun saveRecentQuery(query: String)  = withContext(Dispatchers.IO){
        val queries = localDataSource.getRecentQueries()
        if(queries.size > QUERY_LIMIT) {
            localDataSource.deleteRecentQuery(queries.last())
        }
        localDataSource.saveRecentQuery(RecentQueriesEntity(query = query))
    }

    override suspend fun getRecentQueries(): List<String>  = withContext(Dispatchers.IO){
        localDataSource.getRecentQueries().map { it.query }.reversed()
    }

    override suspend fun getPopularQueries(forceRefresh: Boolean): List<String> = withContext(Dispatchers.IO){

        var queries = localDataSource.getPopularQueries().map { it.query }

        if (forceRefresh || queries.isEmpty()) {
            queries = mboumDataSource.getTrendingStocks().tickers.take(QUERY_LIMIT)
            localDataSource.savePopularQueries(queries.map { PopularQueriesEntity(query = it) })
        }

        queries
    }
}

const val QUERY_LIMIT = 30