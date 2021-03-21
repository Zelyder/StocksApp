package com.zelyder.stocksapp.data.storage.dao

import androidx.room.*
import com.zelyder.stocksapp.data.storage.DbContract
import com.zelyder.stocksapp.data.storage.entities.FavoriteEntity
import com.zelyder.stocksapp.data.storage.entities.StockEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM ${DbContract.Favorites.TABLE_NAME}")
    suspend fun getAllStocks(): List<FavoriteEntity>

    @Query("SELECT * FROM ${DbContract.Favorites.TABLE_NAME} WHERE ${DbContract.Stocks.COLUMN_NAME_TICKER} == :ticker")
    suspend fun getStockByTicker(ticker: String): FavoriteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllStocks(stocks: List<FavoriteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStock(stock: FavoriteEntity)

    @Update
    suspend fun updateStock(stock: FavoriteEntity)

    @Query("UPDATE ${DbContract.Favorites.TABLE_NAME} SET ${DbContract.Stocks.COLUMN_NAME_FAVORITE} = :isFavorite WHERE ${DbContract.Stocks.COLUMN_NAME_TICKER} == :ticker")
    suspend fun updateIsFavoriteByTicker(ticker: String, isFavorite: Boolean)

    @Query("DELETE FROM ${DbContract.Favorites.TABLE_NAME} WHERE ${DbContract.Stocks.COLUMN_NAME_TICKER} == :ticker")
    suspend fun deleteByTicker(ticker: String)

    @Query("DELETE FROM ${DbContract.Favorites.TABLE_NAME}")
    fun deleteAll()
}