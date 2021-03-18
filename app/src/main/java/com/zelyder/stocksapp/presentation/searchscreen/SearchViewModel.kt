package com.zelyder.stocksapp.presentation.searchscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.domain.repositories.SearchRepository
import com.zelyder.stocksapp.domain.repositories.StocksListRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.util.*

class SearchViewModel(private val searchRepository: SearchRepository): ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(this::class.java.simpleName, "CoroutineExceptionHandler:$throwable")
    }

    private val _stocksList = MutableLiveData<List<Stock>>()

    val stocksList: LiveData<List<Stock>> get() = _stocksList

    fun searchStock(query: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _stocksList.value = searchRepository.searchStock(query.toUpperCase(Locale.ROOT))
        }
    }


    fun updateFavState(stock: Stock) {
        viewModelScope.launch(coroutineExceptionHandler) {
            searchRepository.updateStocksIsFavoriteAsync(stock)
        }
    }
}