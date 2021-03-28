package com.zelyder.stocksapp.domain.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.zelyder.stocksapp.domain.models.Stock
import kotlinx.coroutines.flow.Flow

interface StocksListRepository {
    fun getStocksAsync(forceRefresh: Boolean = false): Flow<PagingData<Stock>>
    suspend fun updateStocksIsFavoriteAsync(stock: Stock)
    suspend fun getFavoritesAsync(): List<Stock>
}