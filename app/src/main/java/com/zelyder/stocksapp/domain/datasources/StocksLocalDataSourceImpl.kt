package com.zelyder.stocksapp.domain.datasources

import com.zelyder.stocksapp.data.storage.db.StocksDb
import com.zelyder.stocksapp.data.storage.entities.FavoriteEntity
import com.zelyder.stocksapp.data.storage.entities.PopularQueriesEntity
import com.zelyder.stocksapp.data.storage.entities.RecentQueriesEntity
import com.zelyder.stocksapp.data.storage.entities.StockEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StocksLocalDataSourceImpl(private val stocksDb: StocksDb): StocksLocalDataSource {
    override suspend fun getStocksAsync(): List<StockEntity> = withContext(Dispatchers.IO){
        stocksDb.stocksDao().getAllStocks()
    }

    override suspend fun getStockByTicker(ticker: String):StockEntity = withContext(Dispatchers.IO){
        stocksDb.stocksDao().getStockByTicker(ticker)
    }

    override suspend fun saveStocks(stocks: List<StockEntity>) = withContext(Dispatchers.IO){
        stocksDb.stocksDao().addAllStocks(stocks)
    }

    override suspend fun saveStock(stock: StockEntity) = withContext(Dispatchers.IO){
        stocksDb.stocksDao().addStock(stock)
    }

    override suspend fun updateStock(stock: StockEntity) = withContext(Dispatchers.IO){
        stocksDb.stocksDao().updateStock(stock)
    }

    override suspend fun updateStockIsFavorite(ticker: String, isFavorite: Boolean) = withContext(Dispatchers.IO){
        stocksDb.stocksDao().updateIsFavoriteByTicker(ticker, isFavorite)
    }

    override suspend fun deleteStockByTicker(ticker: String) = withContext(Dispatchers.IO){
        stocksDb.stocksDao().deleteByTicker(ticker)
    }

    override suspend fun deleteAllStocks() = withContext(Dispatchers.IO){
        stocksDb.stocksDao().deleteAll()
    }

    override suspend fun getFavoriteStockByTicker(ticker: String): FavoriteEntity  = withContext(Dispatchers.IO){
        stocksDb.favoriteDao().getStockByTicker(ticker)
    }

    override suspend fun getFavoritesStocks(): List<FavoriteEntity> = withContext(Dispatchers.IO){
        stocksDb.favoriteDao().getAllStocks()
    }

    override suspend fun addFavoriteStock(stock: FavoriteEntity) = withContext(Dispatchers.IO){
        stocksDb.favoriteDao().addStock(stock)
    }

    override suspend fun deleteFavoriteStockByTicker(ticker: String) = withContext(Dispatchers.IO){
        stocksDb.favoriteDao().deleteByTicker(ticker)
    }

    override suspend fun getRecentQueries(): List<RecentQueriesEntity> = withContext(Dispatchers.IO){
        stocksDb.recentQueriesDao().getAllQueries()
    }

    override suspend fun saveRecentQueries(recentQueries: List<RecentQueriesEntity>) = withContext(Dispatchers.IO){
        stocksDb.recentQueriesDao().addAllQueries(recentQueries)
    }

    override suspend fun saveRecentQuery(recentQuery: RecentQueriesEntity) = withContext(Dispatchers.IO){
        stocksDb.recentQueriesDao().addQuery(recentQuery)
    }

    override suspend fun deleteRecentQuery(recentQuery: RecentQueriesEntity) = withContext(Dispatchers.IO){
        stocksDb.recentQueriesDao().deleteQuery(recentQuery)
    }

    override suspend fun getPopularQueries(): List<PopularQueriesEntity> = withContext(Dispatchers.IO){
        stocksDb.popularQueriesDao().getAllQueries()
    }

    override suspend fun savePopularQueries(popularQueries: List<PopularQueriesEntity>) = withContext(Dispatchers.IO){
        stocksDb.popularQueriesDao().addAllQueries(popularQueries)
    }

    override suspend fun savePopularQuery(popularQuery: PopularQueriesEntity) = withContext(Dispatchers.IO){
        stocksDb.popularQueriesDao().addQuery(popularQuery)
    }

    override suspend fun cutOffPopularQueriesAfter(index: Int) = withContext(Dispatchers.IO){
        stocksDb.popularQueriesDao().cutOffAfter(index)
    }
}