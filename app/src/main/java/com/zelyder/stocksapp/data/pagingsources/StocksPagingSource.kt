package com.zelyder.stocksapp.data.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zelyder.stocksapp.data.PAGE_SIZE
import com.zelyder.stocksapp.data.STARTING_PAGE_INDEX
import com.zelyder.stocksapp.data.mappers.toEntity
import com.zelyder.stocksapp.data.mappers.toStock
import com.zelyder.stocksapp.data.mappers.updatePrice
import com.zelyder.stocksapp.data.network.dto.finnhub.StockPriceDto
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSource
import com.zelyder.stocksapp.domain.datasources.StocksFmpDataSource
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSource
import com.zelyder.stocksapp.domain.models.Stock
import retrofit2.HttpException
import java.io.IOException

class StocksPagingSource(
    private val localDataSource: StocksLocalDataSource,
    private val fmpDataSource: StocksFmpDataSource,
    private val finnhubDataSource: StocksFinnhubDataSource,
    private val forceRefresh: Boolean,
) : PagingSource<Int, Stock>() {
    override fun getRefreshKey(state: PagingState<Int, Stock>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Stock> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            var stocks = localDataSource.getStocksAsync().map { it.toStock() }

            if (stocks.isNullOrEmpty()) {
                localDataSource.saveStocks(fmpDataSource.getNasdaqConstituent().map {
                    it.toEntity(
                        StockPriceDto(),
                        localDataSource.getFavoriteStockByTicker(it.symbol) != null
                    )
                })
                stocks = localDataSource.getStocksAsync().map { it.toStock() }
            }
            // Pagination data
            val stockChunked = stocks.chunked(PAGE_SIZE)
            var pageStocks: List<Stock> = emptyList()
            if (position-1 < stockChunked.size){
                pageStocks = stockChunked[position-1]
                // We update the list from the Internet in the case of the first initialization or forced update
                if(pageStocks.any{it.price == 0.0f} || forceRefresh) {
                    pageStocks.onEach { it.updatePrice(finnhubDataSource.getPriceByTicker(it.ticker)) }
                    localDataSource.updateStocks(pageStocks.map { it.toEntity() })
                }
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