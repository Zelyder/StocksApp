package com.zelyder.stocksapp.data.network.apis

import com.zelyder.stocksapp.data.MBOUM_API_KEY
import com.zelyder.stocksapp.data.network.dto.mboum.MostActivesStocksDto
import com.zelyder.stocksapp.data.network.dto.mboum.TrendingStocksDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MboumApi {
    @GET("tr/trending?apikey=$MBOUM_API_KEY")
    suspend fun getTrendingStocks(): List<TrendingStocksDto>

    @GET("co/collections/?list=most_actives&apikey=$MBOUM_API_KEY")
    suspend fun getMostActivesStocks(@Query("start") startPage: Int = 0): MostActivesStocksDto
}