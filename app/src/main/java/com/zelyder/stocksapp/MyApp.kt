package com.zelyder.stocksapp

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.zelyder.stocksapp.data.network.StocksNetworkModule
import com.zelyder.stocksapp.data.storage.db.StocksDb
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSourceImpl
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSourceImpl
import com.zelyder.stocksapp.domain.datasources.StocksMboumDataSourceImpl
import com.zelyder.stocksapp.domain.repositories.*
import com.zelyder.stocksapp.presentation.core.ViewModelFactory
import com.zelyder.stocksapp.presentation.core.ViewModelFactoryProvider
import kotlinx.serialization.ExperimentalSerializationApi

class MyApp : Application(), ViewModelFactoryProvider {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var stocksListRepository: StocksListRepository
    private lateinit var searchRepository: SearchRepository
    private lateinit var detailsRepository: DetailsRepository

    @ExperimentalSerializationApi
    override fun onCreate() {
        super.onCreate()

        initRepositories()

        viewModelFactory = ViewModelFactory(stocksListRepository, searchRepository, detailsRepository)

    }

    @ExperimentalSerializationApi
    private fun initRepositories() {

        val mboumDataSource = StocksMboumDataSourceImpl(StocksNetworkModule().mboumApi())
        val finnhubDataSource = StocksFinnhubDataSourceImpl(StocksNetworkModule().finnhubApi())
        val localDataSource = StocksLocalDataSourceImpl(StocksDb.create(applicationContext))

        stocksListRepository =
            StocksListRepositoryImpl(mboumDataSource, localDataSource)
        searchRepository = SearchRepositoryImpl(mboumDataSource, finnhubDataSource, localDataSource)
        detailsRepository = DetailsRepositoryImpl(finnhubDataSource, localDataSource)
    }

    override fun viewModelFactory(): ViewModelFactory = viewModelFactory
}

fun Context.viewModelFactoryProvider() = (applicationContext as MyApp)

fun Fragment.viewModelFactoryProvider() = requireContext().viewModelFactoryProvider()