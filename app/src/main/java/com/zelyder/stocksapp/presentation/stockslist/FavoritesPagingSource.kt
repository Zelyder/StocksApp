package com.zelyder.stocksapp.presentation.stockslist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zelyder.stocksapp.data.PAGE_SIZE
import com.zelyder.stocksapp.data.STARTING_PAGE_INDEX
import com.zelyder.stocksapp.data.mappers.toEntity
import com.zelyder.stocksapp.data.mappers.toFavoriteStock
import com.zelyder.stocksapp.data.mappers.toStock
import com.zelyder.stocksapp.data.mappers.updatePrice
import com.zelyder.stocksapp.data.network.dto.finnhub.StockPriceDto
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.models.Stock
import retrofit2.HttpException
import java.io.IOException

class FavoritesPagingSource(
    private val localDataSource: StocksLocalDataSource,
    private val finnhubDataSource: StocksFinnhubDataSource
) : PagingSource<Int, Stock>() {
    override fun getRefreshKey(state: PagingState<Int, Stock>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Stock> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val stocks = localDataSource.getFavoritesStocks()?.map { it.toStock() } ?: listOf()

            var pageStocks: List<Stock> = emptyList()
            val stockChunked = stocks.chunked(PAGE_SIZE)
            if (position-1 < stockChunked.size){
                pageStocks = stockChunked[position-1]
                pageStocks.onEach { it.updatePrice(finnhubDataSource.getPriceByTicker(it.ticker)) }
            }
            val nextKey = if (pageStocks.isEmpty()) {
                null
            } else {
                localDataSource.updateFavoritesStock(pageStocks.map { it.toFavoriteStock()})
                position + (params.loadSize / PAGE_SIZE)
            }
                LoadResult.Page(
                    data = pageStocks,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = nextKey
                )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}