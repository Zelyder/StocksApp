package com.zelyder.stocksapp.presentation.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelyder.stocksapp.domain.enums.SelectedItem
import com.zelyder.stocksapp.domain.models.StockCandle
import com.zelyder.stocksapp.domain.repositories.DetailsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class DetailsViewModel(private val detailsRepository: DetailsRepository): ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(this::class.java.simpleName, "CoroutineExceptionHandler:$throwable")
    }


    private val _stockCandle = MutableLiveData<StockCandle>()
    val stockCandle: LiveData<StockCandle> get() = _stockCandle


    override fun onCleared() {
        viewModelScope.launch(coroutineExceptionHandler) {
            detailsRepository.closeSocket()
        }
        super.onCleared()
    }

    fun uploadChart(ticker: String, selectedItem: SelectedItem) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _stockCandle.value = detailsRepository.getStockCandles(ticker, selectedItem)
        }
    }

    // Here I tried to add Web Socket
    @ExperimentalCoroutinesApi
    fun subscribeToSocketEvents(ticker: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                detailsRepository.startSocket(ticker).consumeEach {
                    if (it.exception == null) {
                        println("Collecting : ${it.text}")
                    } else {
                        onSocketError(it.exception)
                    }
                }
            } catch (ex: java.lang.Exception) {
                onSocketError(ex)
            }
        }
    }

    fun closeSocket(){
        viewModelScope.launch(coroutineExceptionHandler) {
            detailsRepository.closeSocket()
        }
    }

    private fun onSocketError(ex: Throwable) {
        println("Error occurred : ${ex.message}")
    }
}