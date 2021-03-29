package com.zelyder.stocksapp.data.network.apis

import com.zelyder.stocksapp.data.network.dto.fmp.NasdaqConstituentDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FmpApi {

    @GET("nasdaq_constituent")
    suspend fun getNasdaqConstituent(@Query("apikey") apiKey: String): List<NasdaqConstituentDto>
}