package com.zelyder.stocksapp.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zelyder.stocksapp.domain.repositories.SearchRepository
import com.zelyder.stocksapp.domain.repositories.StocksListRepository
import com.zelyder.stocksapp.presentation.searchscreen.SearchViewModel
import com.zelyder.stocksapp.presentation.stockslist.StocksListViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val stocksListRepository: StocksListRepository,
    private val searchRepository: SearchRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T  = when(modelClass){
        StocksListViewModel::class.java -> StocksListViewModel(stocksListRepository)
        SearchViewModel::class.java -> SearchViewModel(searchRepository)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T

}