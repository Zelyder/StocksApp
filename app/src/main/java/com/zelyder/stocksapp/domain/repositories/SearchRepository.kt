package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.domain.models.Stock

interface SearchRepository {
    suspend fun searchStock(query: String): List<Stock>
    suspend fun updateStocksIsFavoriteAsync(stock: Stock)
    suspend fun saveRecentQuery(query: String)
    suspend fun getRecentQueries(): List<String>
    suspend fun getPopularQueries(forceRefresh: Boolean = false): List<String>
}