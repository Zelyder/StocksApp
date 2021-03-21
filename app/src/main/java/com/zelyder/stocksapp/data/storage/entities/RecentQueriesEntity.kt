package com.zelyder.stocksapp.data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zelyder.stocksapp.data.storage.DbContract

@Entity(
    tableName = DbContract.RecentQueries.TABLE_NAME
)
data class RecentQueriesEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = DbContract.RecentQueries.COLUMN_NAME_QUERY)
    val query: String = ""
)
