package com.zelyder.stocksapp.presentation.searchscreen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zelyder.stocksapp.data.PAGE_SIZE
import com.zelyder.stocksapp.data.STARTING_PAGE_INDEX
import com.zelyder.stocksapp.data.mappers.toEntity
import com.zelyder.stocksapp.data.mappers.toStock
import com.zelyder.stocksapp.data.mappers.updatePrice
import com.zelyder.stocksapp.data.network.dto.finnhub.StockPriceDto
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.models.Stock
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource(
    private val localDataSource: StocksLocalDataSource,
    private val finnhubDataSource: StocksFinnhubDataSource,
    private val query: String
) : PagingSource<Int, Stock>(){

    override fun getRefreshKey(state: PagingState<Int, Stock>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Stock> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            // Remove regional stocks
            val stocks = finnhubDataSource.searchStock(query).foundStocks.filter { !it.symbol.contains(".") }.map {
                it.toStock(
                    StockPriceDto(),
                    localDataSource.getFavoriteStockByTicker(it.symbol) != null
                )
            }.chunked(PAGE_SIZE)

            var pageStocks: List<Stock> = emptyList()
            if (position-1 < stocks.size){
                pageStocks = stocks[position-1]
                pageStocks.onEach { it.updatePrice(finnhubDataSource.getPriceByTicker(it.ticker)) }
            }
            val nextKey = if (pageStocks.isEmpty()) {
                null
            } else {
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