package com.zelyder.stocksapp.presentation.details

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelyder.stocksapp.domain.enums.SelectedItem
import com.zelyder.stocksapp.domain.models.News
import com.zelyder.stocksapp.domain.models.Ratio
import com.zelyder.stocksapp.domain.models.StockCandle
import com.zelyder.stocksapp.domain.repositories.DetailsRepository
import com.zelyder.stocksapp.presentation.core.fillWithData
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

    private val _ratios = MutableLiveData<List<Ratio>>()
    val ratios: LiveData<List<Ratio>> get() = _ratios

    private val _news = MutableLiveData<List<News>>()
    val news: LiveData<List<News>> get() = _news


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

    // FIXME: On newer versions android does not return data
    fun uploadRatios(ticker: String, resources: Resources) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _ratios.value = detailsRepository.getRatios(ticker).map { it.fillWithData(resources) }
        }
    }

    fun uploadNews(ticker: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _news.value = detailsRepository.getNews(ticker)
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