package com.zelyder.stocksapp.domain.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zelyder.stocksapp.data.PAGE_SIZE
import com.zelyder.stocksapp.data.mappers.toFavoriteStock
import com.zelyder.stocksapp.data.storage.entities.PopularQueriesEntity
import com.zelyder.stocksapp.data.storage.entities.RecentQueriesEntity
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.datasources.StocksMboumDataSource
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.data.pagingsources.SearchPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    private val mboumDataSource: StocksMboumDataSource,
    private val finnhubDataSource: StocksFinnhubDataSource,
    private val localDataSource: StocksLocalDataSource
) : SearchRepository {
    override fun searchStock(query: String): Flow<PagingData<Stock>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchPagingSource(localDataSource, finnhubDataSource, query) }
        ).flow
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
        // Limiting the number of stored recent requests
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