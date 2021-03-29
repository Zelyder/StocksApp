package com.zelyder.stocksapp.presentation.searchscreen

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.domain.repositories.SearchRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(this::class.java.simpleName, "CoroutineExceptionHandler:$throwable")
    }

    private val _recentQueries = MutableLiveData<List<String>>()
    val recentQueries: LiveData<List<String>> get() = _recentQueries

    private val _popularQueries = MutableLiveData<List<String>>()
    val popularQueries: LiveData<List<String>> get() = _popularQueries


    private var stocksList: Flow<PagingData<Stock>>? = null
    private var currentQueryValue: String? = null


    @FlowPreview
    fun searchStock(query: String): Flow<PagingData<Stock>> {
        val lastResult = stocksList
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val newResult: Flow<PagingData<Stock>> =
            searchRepository.searchStock(query.toUpperCase(Locale.ROOT)).cachedIn(viewModelScope)
        stocksList = newResult
        return newResult

    }


    fun updateFavState(stock: Stock) {
        viewModelScope.launch(coroutineExceptionHandler) {
            searchRepository.updateStocksIsFavoriteAsync(stock)
        }
    }

    fun saveQuery(query: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            searchRepository.saveRecentQuery(query)
        }
    }

    fun updateRecentQueries() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _recentQueries.value = searchRepository.getRecentQueries()
        }
    }

    fun updatePopularQueries() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _popularQueries.value = searchRepository.getPopularQueries()
        }
    }
}
