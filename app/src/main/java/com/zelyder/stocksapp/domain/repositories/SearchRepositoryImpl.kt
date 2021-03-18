package com.zelyder.stocksapp.domain.repositories

import android.util.Log
import com.zelyder.stocksapp.data.mappers.toEntity
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
        val stocks = finnhubDataSource.searchStock(query).foundStocks.filter { !it.symbol.contains(".") }.map {
            it.toStock(finnhubDataSource.getPriceByTicker(it.symbol),
                localDataSource.getFavoriteStockByTicker(it.symbol) != null
            )
        }
        Log.d("LOL", stocks.toString())
        stocks
    }

    override suspend fun updateStocksIsFavoriteAsync(stock: Stock) =
        withContext(Dispatchers.IO) {
            if (stock.isFavorite) {
                localDataSource.addFavoriteStock(stock.toFavoriteStock())
            } else {
                localDataSource.deleteFavoriteStockByTicker(stock.ticker)
            }

        }
}