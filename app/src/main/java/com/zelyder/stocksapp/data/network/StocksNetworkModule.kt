package com.zelyder.stocksapp.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zelyder.stocksapp.data.FINNHUB_BASE_URL
import com.zelyder.stocksapp.data.FMP_BASE_URL
import com.zelyder.stocksapp.data.MBOUM_BASE_URL
import com.zelyder.stocksapp.data.network.apis.FinnhubApi
import com.zelyder.stocksapp.data.network.apis.FmpApi
import com.zelyder.stocksapp.data.network.apis.MboumApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create

@ExperimentalSerializationApi
class StocksNetworkModule: StocksNetworkClient {

    private val jsonFormat = Json {
        ignoreUnknownKeys = true
    }

    private val  finnhubRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(FINNHUB_BASE_URL)
        .addConverterFactory(jsonFormat.asConverterFactory("application/json".toMediaType()))
        .build()

    private val  mboumRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(MBOUM_BASE_URL)
        .addConverterFactory(jsonFormat.asConverterFactory("application/json".toMediaType()))
        .build()

    private val  fmpRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(FMP_BASE_URL)
        .addConverterFactory(jsonFormat.asConverterFactory("application/json".toMediaType()))
        .build()

    override fun finnhubApi(): FinnhubApi = finnhubRetrofit.create()

    override fun mboumApi(): MboumApi = mboumRetrofit.create()

    override fun fmpApi(): FmpApi = fmpRetrofit.create()
}