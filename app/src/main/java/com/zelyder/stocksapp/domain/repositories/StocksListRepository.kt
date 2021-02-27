package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.domain.models.Stock

interface StocksListRepository {
    suspend fun getStocksAsync(forceRefresh: Boolean = false): List<Stock>
}