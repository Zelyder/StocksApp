package com.zelyder.stocksapp.domain.repositories

import androidx.paging.PagingData
import com.zelyder.stocksapp.domain.models.Stock
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchStock(query: String): Flow<PagingData<Stock>>
    suspend fun updateStocksIsFavoriteAsync(stock: Stock)
    suspend fun saveRecentQuery(query: String)
    suspend fun getRecentQueries(): List<String>
    suspend fun getPopularQueries(forceRefresh: Boolean = false): List<String>
}