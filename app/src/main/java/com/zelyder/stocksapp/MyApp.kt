package com.zelyder.stocksapp

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.zelyder.stocksapp.data.network.StocksNetworkModule
import com.zelyder.stocksapp.domain.datasources.StocksRemoteDataSourceImpl
import com.zelyder.stocksapp.domain.repositories.StocksListRepository
import com.zelyder.stocksapp.domain.repositories.StocksListRepositoryImpl
import com.zelyder.stocksapp.presentation.core.ViewModelFactory
import com.zelyder.stocksapp.presentation.core.ViewModelFactoryProvider
import kotlinx.serialization.ExperimentalSerializationApi

class MyApp: Application(), ViewModelFactoryProvider {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var stocksListRepository: StocksListRepository

    @ExperimentalSerializationApi
    override fun onCreate() {
        super.onCreate()

        initRepositories()

        viewModelFactory = ViewModelFactory(stocksListRepository)

    }

    @ExperimentalSerializationApi
    private fun initRepositories() {

        val remoteDataSource = StocksRemoteDataSourceImpl(StocksNetworkModule().mboumApi())

        stocksListRepository = StocksListRepositoryImpl(remoteDataSource)
    }

    override fun viewModelFactory(): ViewModelFactory = viewModelFactory
}

fun Context.viewModelFactoryProvider() = (applicationContext as MyApp)

fun Fragment.viewModelFactoryProvider() = requireContext().viewModelFactoryProvider()