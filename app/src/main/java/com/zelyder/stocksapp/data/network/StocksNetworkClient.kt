package com.zelyder.stocksapp.data.network

import com.zelyder.stocksapp.data.network.apis.FinnhubApi
import com.zelyder.stocksapp.data.network.apis.MboumApi

interface StocksNetworkClient {
    fun finnhubApi(): FinnhubApi
    fun mboumApi(): MboumApi
}