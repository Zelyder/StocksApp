package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.domain.models.Stock

interface SearchRepository {
    suspend fun searchStock(query: String): List<Stock>
    suspend fun updateStocksIsFavoriteAsync(stock: Stock)
}