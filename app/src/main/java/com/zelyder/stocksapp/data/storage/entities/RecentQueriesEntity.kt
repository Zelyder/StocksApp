package com.zelyder.stocksapp.data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zelyder.stocksapp.data.storage.DbContract

@Entity(tableName = DbContract.RecentQueries.TABLE_NAME)
data class RecentQueriesEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DbContract.RecentQueries.COLUMN_NAME_ID)
    val id: Int = 0,
    @ColumnInfo(name = DbContract.RecentQueries.COLUMN_NAME_QUERY)
    val query: String = ""
)
