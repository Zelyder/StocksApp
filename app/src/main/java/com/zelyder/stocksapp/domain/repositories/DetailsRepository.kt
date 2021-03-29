package com.zelyder.stocksapp.domain.repositories

import com.zelyder.stocksapp.data.network.SocketUpdate
import com.zelyder.stocksapp.domain.enums.SelectedItem
import com.zelyder.stocksapp.domain.models.StockCandle
import kotlinx.coroutines.channels.Channel

interface DetailsRepository {
    suspend fun getStockCandles(ticker: String, selectedItem: SelectedItem):StockCandle
    suspend fun closeSocket()
    suspend fun startSocket(ticker: String): Channel<SocketUpdate>
}