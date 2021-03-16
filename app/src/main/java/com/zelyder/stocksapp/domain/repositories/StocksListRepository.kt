package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.domain.models.Stock

interface StocksListRepository {
    suspend fun getStocksAsync(forceRefresh: Boolean = false): List<Stock>
    suspend fun updateStocksIsFavoriteAsync(ticker: String, isFavorite: Boolean)
    suspend fun getFavoritesAsync(): List<Stock>
    suspend fun searchStock(query: String): List<Stock>
}