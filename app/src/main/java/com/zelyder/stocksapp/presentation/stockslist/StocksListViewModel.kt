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
    private  val coroutineExceptionHandler = CoroutineExceptionHandler {_, throwable ->
        Log.e(this::class.java.simpleName, "CoroutineExceptionHandler:$throwable")
    }

    private val _stocksList = MutableLiveData<List<Stock>>()

    val stocksList: LiveData<List<Stock>> get() = _stocksList

    fun updateList() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _stocksList.value = stocksListRepository.getStocksAsync()
        }
    }
}