package com.zelyder.stocksapp.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zelyder.stocksapp.domain.repositories.DetailsRepository
import com.zelyder.stocksapp.domain.repositories.SearchRepository
import com.zelyder.stocksapp.domain.repositories.StocksListRepository
import com.zelyder.stocksapp.presentation.details.DetailsViewModel
import com.zelyder.stocksapp.presentation.searchscreen.SearchViewModel
import com.zelyder.stocksapp.presentation.stockslist.StocksListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val stocksListRepository: StocksListRepository,
    private val searchRepository: SearchRepository,
    private val detailsRepository: DetailsRepository
) : ViewModelProvider.Factory {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T  = when(modelClass){
        StocksListViewModel::class.java -> StocksListViewModel(stocksListRepository)
        SearchViewModel::class.java -> SearchViewModel(searchRepository)
        DetailsViewModel::class.java -> DetailsViewModel(detailsRepository)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T

}