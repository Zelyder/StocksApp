package com.zelyder.stocksapp.data.network.apis

import com.zelyder.stocksapp.data.network.dto.fmp.NasdaqConstituentDto
import com.zelyder.stocksapp.data.network.dto.fmp.NewsDto
import com.zelyder.stocksapp.data.network.dto.fmp.RatiosDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FmpApi {

    @GET("nasdaq_constituent")
    suspend fun getNasdaqConstituent(@Query("apikey") apiKey: String): List<NasdaqConstituentDto>

    @GET("ratios-ttm/{ticker}")
    suspend fun getRatios(
        @Path("ticker") ticker: String,
        @Query("apikey") apiKey: String
    ): List<RatiosDto>

    @GET("stock_news")
    suspend fun getNews(
        @Query("apikey") apiKey: String,
        @Query("tickers") ticker: String,
        @Query("limit") limit: Int
    ): List<NewsDto>
}