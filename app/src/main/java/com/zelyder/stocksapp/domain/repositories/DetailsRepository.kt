package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.domain.models.StockCandle

interface DetailsRepository {
    suspend fun getStockCandles(ticker: String):StockCandle
}