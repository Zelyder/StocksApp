package com.zelyder.stocksapp.domain.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.zelyder.stocksapp.data.LOGO_BASE_URL
import com.zelyder.stocksapp.data.PAGE_SIZE
import com.zelyder.stocksapp.data.mappers.toEntity
import com.zelyder.stocksapp.data.mappers.toFavoriteStock
import com.zelyder.stocksapp.data.mappers.toStock
import com.zelyder.stocksapp.data.mappers.toStockEntity
import com.zelyder.stocksapp.data.network.dto.finnhub.StockPriceDto
import com.zelyder.stocksapp.data.storage.db.StocksPagingSource
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksFmpDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.datasources.StocksMboumDataSource
import com.zelyder.stocksapp.domain.models.Stock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.math.round

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
            pagingSourceFactory = { StocksPagingSource(localDataSource, fmpDataSource, finnhubDataSource) }
        ).flow

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