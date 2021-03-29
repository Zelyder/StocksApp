package com.zelyder.stocksapp.presentation.stockslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.domain.repositories.StocksListRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class StocksListViewModel(private val stocksListRepository: StocksListRepository) : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(this::class.java.simpleName, "CoroutineExceptionHandler:$throwable")
    }

    private var _stocksList: Flow<PagingData<Stock>>? = null
    private val _isFavSelected = MutableLiveData<Boolean?>()

    val isFavSelected: LiveData<Boolean?> get() = _isFavSelected

    fun updatedList(forceRefresh: Boolean = false): Flow<PagingData<Stock>> {
        val newResult: Flow<PagingData<Stock>> = stocksListRepository.getStocksAsync(forceRefresh).cachedIn(viewModelScope)
        _stocksList = newResult
            return newResult
    }

    fun swapToStocksTab(): Flow<PagingData<Stock>>? {
        if (_isFavSelected.value == true) {
            _isFavSelected.value = false
            return updatedList()
        }
        return null
    }

    fun updateFavState(stock: Stock) {

        viewModelScope.launch(coroutineExceptionHandler) {
            stocksListRepository.updateStocksIsFavoriteAsync(stock)
        }
    }

    fun swapToFavTab(forceRefresh: Boolean = false):Flow<PagingData<Stock>>? {
        if (_isFavSelected.value == false || _isFavSelected.value == null || forceRefresh) {
            _isFavSelected.value = true
            val newResult: Flow<PagingData<Stock>> = stocksListRepository.getFavoritesAsync(forceRefresh).cachedIn(viewModelScope)
            _stocksList = newResult
            return newResult
        }
        return null
    }

    fun resetTabState() {
        _isFavSelected.value = false
    }
}