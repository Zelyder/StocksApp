package com.zelyder.stocksapp.data.storage.dao

import androidx.room.*
import com.zelyder.stocksapp.data.storage.DbContract
import com.zelyder.stocksapp.data.storage.entities.RecentQueriesEntity

@Dao
interface RecentQueriesDao {

    @Query("SELECT * FROM ${DbContract.RecentQueries.TABLE_NAME}")
    suspend fun getAllQueries(): List<RecentQueriesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllQueries(stocks: List<RecentQueriesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuery(stock: RecentQueriesEntity)

    @Query("DELETE FROM ${DbContract.RecentQueries.TABLE_NAME} WHERE ${DbContract.RecentQueries.COLUMN_NAME_QUERY} == :query")
    suspend fun deleteByQuery(query: String)

    @Query("DELETE FROM ${DbContract.RecentQueries.TABLE_NAME}")
    fun deleteAll()

    @Delete
    fun deleteQuery(recentQueriesEntity: RecentQueriesEntity)

}