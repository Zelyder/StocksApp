package com.zelyder.stocksapp.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zelyder.stocksapp.data.storage.DbContract
import com.zelyder.stocksapp.data.storage.entities.RemoteKeysEntity

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysEntity>)

    @Query("SELECT * FROM ${DbContract.RemoteKeys.TABLE_NAME} WHERE ${DbContract.RemoteKeys.COLUMN_NAME_TICKER} = :ticker")
    suspend fun remoteKeysRepoId(ticker: String): RemoteKeysEntity?

    @Query("DELETE FROM ${DbContract.RemoteKeys.TABLE_NAME}")
    suspend fun clearRemoteKeys()
}