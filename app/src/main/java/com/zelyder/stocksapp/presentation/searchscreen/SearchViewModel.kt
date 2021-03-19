package com.zelyder.stocksapp.presentation.searchscreen

import android.util.Log
import androidx.lifecycle.*
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
class SearchViewModel(private val searchRepository: SearchRepository): ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(this::class.java.simpleName, "CoroutineExceptionHandler:$throwable")
    }

    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState> get() = _searchState


    private val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    @FlowPreview
    val stocksList: LiveData<SearchResult> get() = queryChannel
        .asFlow()
        .onEach { _searchState.value = Loading }
        .mapLatest {
            if (it.isEmpty()) {
                EmptyQuery
            } else {
                try {
                    val result = searchRepository.searchStock(it.toUpperCase(Locale.ROOT))
                    if( result.isEmpty()) {
                        EmptyResult
                    } else {
                        ValidResult(result)
                    }
                } catch (e: Throwable) {
                    if (e is CancellationException) {
                        throw e
                    } else {
                        Log.w(this::class.java.name, e)
                        ErrorResult(e)
                    }
                }
            }
        }
        .onEach { _searchState.value = Ready }
        .catch { emit(TerminalError) }
        .asLiveData(viewModelScope.coroutineContext)



    fun searchStock(query: String) {

        viewModelScope.launch(coroutineExceptionHandler) {
            queryChannel.send(query)
        }
    }


    fun updateFavState(stock: Stock) {
        viewModelScope.launch(coroutineExceptionHandler) {
            searchRepository.updateStocksIsFavoriteAsync(stock)
        }
    }
}
