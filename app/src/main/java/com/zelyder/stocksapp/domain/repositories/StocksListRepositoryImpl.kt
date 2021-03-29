package com.zelyder.stocksapp.domain.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zelyder.stocksapp.data.PAGE_SIZE
import com.zelyder.stocksapp.data.mappers.toFavoriteStock
import com.zelyder.stocksapp.data.pagingsources.StocksPagingSource
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksFmpDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.data.pagingsources.FavoritesPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class StocksListRepositoryImpl(
    private val fmpDataSource: StocksFmpDataSource,
    private val finnhubDataSource: StocksFinnhubDataSource,
    private val localDataSource: StocksLocalDataSource
) : StocksListRepository {
    override fun getStocksAsync(forceRefresh: Boolean): Flow<PagingData<Stock>> {

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                StocksPagingSource(
                    localDataSource,
                    fmpDataSource,
                    finnhubDataSource,
                    forceRefresh
                )
            }
        ).flow
    }

    override suspend fun updateStocksIsFavoriteAsync(stock: Stock) = withContext(Dispatchers.IO) {
        localDataSource.updateStockIsFavorite(stock.ticker, stock.isFavorite)
        if (stock.isFavorite) {
            localDataSource.addFavoriteStock(stock.toFavoriteStock())
        } else {
            localDataSource.deleteFavoriteStockByTicker(stock.ticker)
        }

    }

    override fun getFavoritesAsync(forceRefresh: Boolean): Flow<PagingData<Stock>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { FavoritesPagingSource(localDataSource, finnhubDataSource, forceRefresh) }
        ).flow
    }

}