package com.zelyder.stocksapp.data.storage.dao

import androidx.room.*
import com.zelyder.stocksapp.data.storage.DbContract
import com.zelyder.stocksapp.data.storage.entities.StockEntity

@Dao
interface StocksDao {
    @Query("SELECT * FROM ${DbContract.Stocks.TABLE_NAME}")
    suspend fun getAllStocks(): List<StockEntity>

    @Query("SELECT * FROM ${DbContract.Stocks.TABLE_NAME} WHERE ${DbContract.Stocks.COLUMN_NAME_TICKER} == :ticker")
    suspend fun getStockByTicker(ticker: String): StockEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllStocks(stocks: List<StockEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStock(stock: StockEntity)

    @Update
    suspend fun updateStock(stock: StockEntity)

    @Query("UPDATE ${DbContract.Stocks.TABLE_NAME} SET ${DbContract.Stocks.COLUMN_NAME_FAVORITE} = :isFavorite WHERE ${DbContract.Stocks.COLUMN_NAME_TICKER} == :ticker")
    suspend fun updateIsFavoriteByTicker(ticker: String, isFavorite: Boolean)

    @Query("DELETE FROM ${DbContract.Stocks.TABLE_NAME} WHERE ${DbContract.Stocks.COLUMN_NAME_TICKER} == :ticker")
    suspend fun deleteByTicker(ticker: String)

    @Query("DELETE FROM ${DbContract.Stocks.TABLE_NAME}")
    fun deleteAll()
}