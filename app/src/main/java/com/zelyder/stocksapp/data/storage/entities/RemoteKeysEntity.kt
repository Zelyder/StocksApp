package com.zelyder.stocksapp.data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zelyder.stocksapp.data.storage.DbContract

@Entity(tableName = DbContract.RemoteKeys.TABLE_NAME)
data class RemoteKeysEntity(
    @PrimaryKey
    @ColumnInfo(name = DbContract.RemoteKeys.COLUMN_NAME_TICKER)
    val ticker: String,
    @ColumnInfo(name = DbContract.RemoteKeys.COLUMN_NAME_PREV_KEY)
    val prevKey: Int?,
    @ColumnInfo(name = DbContract.RemoteKeys.COLUMN_NAME_NEXT_KEY)
    val nextKey: Int?
)
