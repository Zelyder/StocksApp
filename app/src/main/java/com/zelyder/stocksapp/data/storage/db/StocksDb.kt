package com.zelyder.stocksapp.data.storage.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zelyder.stocksapp.data.storage.DbContract
import com.zelyder.stocksapp.data.storage.dao.FavoriteDao
import com.zelyder.stocksapp.data.storage.dao.StocksDao
import com.zelyder.stocksapp.data.storage.entities.FavoriteEntity
import com.zelyder.stocksapp.data.storage.entities.StockEntity

@Database(entities = [StockEntity::class, FavoriteEntity::class], version = 2)
abstract class StocksDb: RoomDatabase() {

    abstract fun stocksDao(): StocksDao
    abstract fun favoriteDao(): FavoriteDao

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