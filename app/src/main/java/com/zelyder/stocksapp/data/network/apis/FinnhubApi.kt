package com.zelyder.stocksapp.data.network.apis

import com.zelyder.stocksapp.data.network.dto.finnhub.*
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

    @GET("stock/candle")
    suspend fun getStockCandles(
        @Query("token") apiKey: String,
        @Query("symbol") ticker: String,
        @Query("resolution") resolution: String,
        @Query("from") fromTimestamp: Long,
        @Query("to") toTimestamp: Long,
    ):StockCandlesDto

    @GET("index/constituents")
    suspend fun getIndexConstituents(
        @Query("token") apiKey: String,
        @Query("symbol") indexSymbol: String
    ): IndexConstituentsDto


}