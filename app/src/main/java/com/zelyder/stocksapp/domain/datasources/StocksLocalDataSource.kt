package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.storage.entities.FavoriteEntity
import com.zelyder.stocksapp.data.storage.entities.PopularQueriesEntity
import com.zelyder.stocksapp.data.storage.entities.RecentQueriesEntity
import com.zelyder.stocksapp.data.storage.entities.StockEntity

interface StocksLocalDataSource {
    suspend fun getStocksAsync(): List<StockEntity>
    suspend fun getStockByTicker(ticker: String): StockEntity
    suspend fun saveStocks(stocks: List<StockEntity>)
    suspend fun saveStock(stock: StockEntity)
    suspend fun updateStock(stock: StockEntity)
    suspend fun updateStockIsFavorite(ticker: String, isFavorite: Boolean)
    suspend fun deleteStockByTicker(ticker: String)
    suspend fun deleteAllStocks()

    suspend fun getFavoriteStockByTicker(ticker: String): FavoriteEntity?
    suspend fun getFavoritesStocks(): List<FavoriteEntity>?
    suspend fun addFavoriteStock(stock: FavoriteEntity)
    suspend fun deleteFavoriteStockByTicker(ticker: String)

    suspend fun getRecentQueries(): List<RecentQueriesEntity>
    suspend fun saveRecentQueries(recentQueries: List<RecentQueriesEntity>)
    suspend fun saveRecentQuery(recentQuery: RecentQueriesEntity)
    suspend fun cutOffRecentQueriesAfter(index: Int)

    suspend fun getPopularQueries(): List<PopularQueriesEntity>
    suspend fun savePopularQueries(popularQueries: List<PopularQueriesEntity>)
    suspend fun savePopularQuery(popularQuery: PopularQueriesEntity)
    suspend fun cutOffPopularQueriesAfter(index: Int)

}