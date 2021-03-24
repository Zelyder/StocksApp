package com.zelyder.stocksapp.presentation.details

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelyder.stocksapp.domain.models.StockCandle
import com.zelyder.stocksapp.domain.repositories.DetailsRepository
import com.zelyder.stocksapp.presentation.stockslist.StocksListViewModel
import com.zelyder.stocksapp.viewModelFactoryProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class DetailsViewModel(private val detailsRepository: DetailsRepository): ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(this::class.java.simpleName, "CoroutineExceptionHandler:$throwable")
    }


    private val _stockCandle = MutableLiveData<StockCandle>()
    val stockCandle: LiveData<StockCandle> get() = _stockCandle

    fun uploadChart(ticker: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _stockCandle.value = detailsRepository.getStockCandles(ticker)
        }
    }
}