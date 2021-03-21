package com.zelyder.stocksapp.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zelyder.stocksapp.data.storage.DbContract
import com.zelyder.stocksapp.data.storage.entities.PopularQueriesEntity

@Dao
interface PopularQueriesDao {

    @Query("SELECT * FROM ${DbContract.PopularQueries.TABLE_NAME}")
    suspend fun getAllQueries(): List<PopularQueriesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllQueries(stocks: List<PopularQueriesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuery(stock: PopularQueriesEntity)

    @Query("DELETE FROM ${DbContract.PopularQueries.TABLE_NAME} WHERE ${DbContract.PopularQueries.COLUMN_NAME_QUERY} == :query")
    suspend fun deleteByQuery(query: String)

    @Query("DELETE FROM ${DbContract.PopularQueries.TABLE_NAME}")
    fun deleteAll()

    @Query("DELETE FROM ${DbContract.PopularQueries.TABLE_NAME} WHERE ${DbContract.PopularQueries.COLUMN_NAME_ID} > :index")
    fun cutOffAfter(index: Int)
}