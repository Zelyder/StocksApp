package com.zelyder.stocksapp.data.network.apis

import com.zelyder.stocksapp.data.network.dto.finnhub.SearchResultDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubApi {

    @GET("search")
    suspend fun searchStock(
        @Query("token") apiKey: String,
        @Query("q") query: String
    ): SearchResultDto

}