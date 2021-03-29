package com.zelyder.stocksapp.data.storage.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zelyder.stocksapp.data.storage.DbContract
import com.zelyder.stocksapp.data.storage.dao.*
import com.zelyder.stocksapp.data.storage.entities.*

@Database(
    entities = [StockEntity::class, FavoriteEntity::class, RecentQueriesEntity::class, PopularQueriesEntity::class],
    version = 8,
    exportSchema = false
)
abstract class StocksDb : RoomDatabase() {

    abstract fun stocksDao(): StocksDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun recentQueriesDao(): RecentQueriesDao
    abstract fun popularQueriesDao(): PopularQueriesDao

    companion object {
        fun create(context: Context): StocksDb = Room.databaseBuilder(
            context,
            StocksDb::class.java,
            DbContract.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}