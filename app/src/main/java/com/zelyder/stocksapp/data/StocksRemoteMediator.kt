package com.zelyder.stocksapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.zelyder.stocksapp.data.storage.db.StocksDb
import com.zelyder.stocksapp.data.storage.entities.RemoteKeysEntity
import com.zelyder.stocksapp.domain.datasources.StocksFmpDataSource
import com.zelyder.stocksapp.domain.models.Stock
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
class StocksRemoteMediator(
    private val service: StocksFmpDataSource,
    private val database: StocksDb
) : RemoteMediator<Int, Stock>() {

    //TODO: try to uncomment
//    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
//    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Stock>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If the previous key is null, then the list is empty so we should wait for data
                // fetched by remote refresh and can simply skip loading this time by returning
                // `false` for endOfPaginationReached.
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = false)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If the next key is null, then the list is empty so we should wait for data
                // fetched by remote refresh and can simply skip loading this time by returning
                // `false` for endOfPaginationReached.
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = false)
                nextKey
            }
        }

        try {
            val stocks = service.getNasdaqConstituent()
            val endOfPaginationReached = stocks.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
//                    database.reposDao().clearRepos()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = stocks.map {
//                    RemoteKeysEntity(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
//                database.remoteKeysDao().insertAll(keys)
//                database.reposDao().insertAll(stocks)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Stock>): RemoteKeysEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { stock ->
                // Get the remote keys of the last item retrieved
                database.remoteKeysDao().remoteKeysRepoId(stock.ticker)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Stock>): RemoteKeysEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { stock ->
                // Get the remote keys of the first items retrieved
                database.remoteKeysDao().remoteKeysRepoId(stock.ticker)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Stock>
    ): RemoteKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.ticker?.let { repoId ->
                database.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }
}