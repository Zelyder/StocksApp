package com.zelyder.stocksapp.presentation.stockslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.domain.repositories.StocksListRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class StocksListViewModel(private val stocksListRepository: StocksListRepository) : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(this::class.java.simpleName, "CoroutineExceptionHandler:$throwable")
    }

    private val _stocksList = MutableLiveData<List<Stock>>()
    private val _isFavSelected = MutableLiveData<Boolean?>()

    val stocksList: LiveData<List<Stock>> get() = _stocksList
    val isFavSelected: LiveData<Boolean?> get() = _isFavSelected

    fun updateList(forceRefresh: Boolean = false) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _stocksList.value = stocksListRepository.getStocksAsync(forceRefresh)
        }
    }

    fun swapToStocksTab() {
        if (_isFavSelected.value == true) {
            _isFavSelected.value = false
            updateList()
        }
    }

    fun updateFavState(stock: Stock) {

        viewModelScope.launch(coroutineExceptionHandler) {
            stocksListRepository.updateStocksIsFavoriteAsync(stock)
        }
    }

    fun swapToFavTab() {
        if (_isFavSelected.value == false || _isFavSelected.value == null) {
            _isFavSelected.value = true
            viewModelScope.launch(coroutineExceptionHandler) {
                _stocksList.value = stocksListRepository.getFavoritesAsync()
            }
        }
    }

    fun resetTabState() {
        _isFavSelected.value = false
    }
}