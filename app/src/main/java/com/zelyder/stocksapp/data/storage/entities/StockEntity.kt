package com.zelyder.stocksapp.data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zelyder.stocksapp.data.storage.DbContract
import java.util.*

@Entity(tableName = DbContract.Stocks.TABLE_NAME)
data class StockEntity(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = DbContract.Stocks.COLUMN_NAME_TICKER)
        val ticker: String = "",
        @ColumnInfo(name = DbContract.Stocks.COLUMN_NAME_COMPANY_NAME)
        val companyName: String = "",
        @ColumnInfo(name = DbContract.Stocks.COLUMN_NAME_LOGO)
        val logo: String = "",
        @ColumnInfo(name = DbContract.Stocks.COLUMN_NAME_PRICE)
        val price: Float = 0.0f,
        @ColumnInfo(name = DbContract.Stocks.COLUMN_NAME_CURRENCY)
        val currency: String = "",
        @ColumnInfo(name = DbContract.Stocks.COLUMN_NAME_DAY_DELTA)
        val dayDelta: Float = 0.0f,
        @ColumnInfo(name = DbContract.Stocks.COLUMN_NAME_DAY_DELTA_PERCENT)
        val dayDeltaPercent: Float = 0.0f,
        @ColumnInfo(name = DbContract.Stocks.COLUMN_NAME_FAVORITE)
        val isFavorite: Boolean = false
)