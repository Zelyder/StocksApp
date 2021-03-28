package com.zelyder.stocksapp.presentation.searchscreen

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.domain.repositories.SearchRepository
import com.zelyder.stocksapp.domain.repositories.StocksListRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.CancellationException

@ExperimentalCoroutinesApi
class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(this::class.java.simpleName, "CoroutineExceptionHandler:$throwable")
    }

    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState> get() = _searchState

    private val _recentQueries = MutableLiveData<List<String>>()
    val recentQueries: LiveData<List<String>> get() = _recentQueries

    private val _popularQueries = MutableLiveData<List<String>>()
    val popularQueries: LiveData<List<String>> get() = _popularQueries

    private val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)


    private var stocksList: Flow<PagingData<Stock>>? = null
//        .asFlow()
//        .onEach { _searchState.value = Loading }
//        .mapLatest {
//            if (it.isEmpty()) {
//                EmptyQuery
//            } else {
//                try {
//                    val result = searchRepository.searchStock(it.toUpperCase(Locale.ROOT))
//                    if( result.count() == 0) {
//                        EmptyResult
//                    } else {
//                        ValidResult(result)
//                    }
//                } catch (e: Throwable) {
//                    if (e is CancellationException) {
//                        throw e
//                    } else {
//                        Log.w(this::class.java.name, e)
//                        ErrorResult(e)
//                    }
//                }
//            }
//        }
//        .onEach { _searchState.value = Ready }
//        .catch { emit(TerminalError) }
//        .asLiveData(viewModelScope.coroutineContext)


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
