package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.data.mappers.toFavoriteStock
import com.zelyder.stocksapp.data.mappers.toStock
import com.zelyder.stocksapp.data.storage.entities.FavoriteEntity
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.models.Stock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    private val finnhubDataSource: StocksFinnhubDataSource,
    private val localDataSource: StocksLocalDataSource
) : SearchRepository {
    override suspend fun searchStock(query: String): List<Stock> = withContext(Dispatchers.IO) {
        finnhubDataSource.searchStock(query).foundStocks.map { it.toStock() }
    }

    override suspend fun updateStocksIsFavoriteAsync(ticker: String, isFavorite: Boolean) =
        withContext(Dispatchers.IO) {
            //val favStock = localDataSource.getStockByTicker(ticker).toFavoriteStock()
            if (isFavorite) {
                localDataSource.addFavoriteStock(FavoriteEntity(ticker = ticker, isFavorite = isFavorite))
            } else {
                localDataSource.deleteFavoriteStockByTicker(ticker)
            }

        }
}