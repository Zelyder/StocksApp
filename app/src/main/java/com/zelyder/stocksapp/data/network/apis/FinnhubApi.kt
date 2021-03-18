package com.zelyder.stocksapp.data.network.apis

import com.zelyder.stocksapp.data.network.dto.finnhub.SearchResultDto
import com.zelyder.stocksapp.data.network.dto.finnhub.StockInfoDto
import com.zelyder.stocksapp.data.network.dto.finnhub.StockPriceDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubApi {

    @GET("search")
    suspend fun searchStock(
        @Query("token") apiKey: String,
        @Query("q") query: String
    ): SearchResultDto

    @GET("stock/profile2")
    suspend fun getInfoByTicker(
        @Query("token") apiKey: String,
        @Query("symbol") ticker: String
    ):StockInfoDto

    @GET("quote")
    suspend fun getPriceByTicker(
        @Query("token") apiKey: String,
        @Query("symbol") ticker: String
    ): StockPriceDto

}