package com.zelyder.stocksapp.data.network

import com.zelyder.stocksapp.data.network.apis.FinnhubApi
import com.zelyder.stocksapp.data.network.apis.FmpApi
import com.zelyder.stocksapp.data.network.apis.MboumApi
import okhttp3.WebSocket

interface StocksNetworkClient {
    fun finnhubApi(): FinnhubApi
    fun mboumApi(): MboumApi
    fun fmpApi(): FmpApi
}