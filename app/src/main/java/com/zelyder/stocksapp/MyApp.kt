package com.zelyder.stocksapp

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.zelyder.stocksapp.data.network.StocksNetworkModule
import com.zelyder.stocksapp.data.network.WebServicesProvider
import com.zelyder.stocksapp.data.storage.db.StocksDb
import com.zelyder.stocksapp.domain.datasources.StocksFinnhubDataSourceImpl
import com.zelyder.stocksapp.domain.datasources.StocksFmpDataSourceImpl
import com.zelyder.stocksapp.domain.datasources.StocksLocalDataSourceImpl
import com.zelyder.stocksapp.domain.datasources.StocksMboumDataSourceImpl
import com.zelyder.stocksapp.domain.repositories.*
import com.zelyder.stocksapp.presentation.core.ViewModelFactory
import com.zelyder.stocksapp.presentation.core.ViewModelFactoryProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi


class MyApp : Application(), ViewModelFactoryProvider {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var stocksListRepository: StocksListRepository
    private lateinit var searchRepository: SearchRepository
    private lateinit var detailsRepository: DetailsRepository

    @ExperimentalSerializationApi
    private lateinit var networkModule: StocksNetworkModule

    @ExperimentalCoroutinesApi
    @ExperimentalSerializationApi
    override fun onCreate() {
        super.onCreate()

        networkModule = StocksNetworkModule()
        initRepositories()

        viewModelFactory =
            ViewModelFactory(stocksListRepository, searchRepository, detailsRepository)

    }


    @ExperimentalCoroutinesApi
    @ExperimentalSerializationApi
    private fun initRepositories() {

        val mboumDataSource = StocksMboumDataSourceImpl(networkModule.mboumApi())
        val finnhubDataSource = StocksFinnhubDataSourceImpl(networkModule.finnhubApi())
        val fmpDataSource = StocksFmpDataSourceImpl(networkModule.fmpApi())
        val localDataSource = StocksLocalDataSourceImpl(StocksDb.create(applicationContext))

        stocksListRepository =
            StocksListRepositoryImpl(fmpDataSource, finnhubDataSource, localDataSource)
        searchRepository = SearchRepositoryImpl(mboumDataSource, finnhubDataSource, localDataSource)
        detailsRepository =
            DetailsRepositoryImpl(finnhubDataSource, fmpDataSource, WebServicesProvider())
    }

    override fun viewModelFactory(): ViewModelFactory = viewModelFactory
}

fun Context.viewModelFactoryProvider() = (applicationContext as MyApp)

fun Fragment.viewModelFactoryProvider() = requireContext().viewModelFactoryProvider()