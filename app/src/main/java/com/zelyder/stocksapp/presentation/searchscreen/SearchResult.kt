package com.zelyder.stocksapp.presentation.searchscreen

import com.zelyder.stocksapp.domain.models.Stock

sealed class SearchResult
data class ValidResult(val result: List<Stock>) : SearchResult()
object EmptyResult : SearchResult()
object EmptyQuery : SearchResult()
data class ErrorResult(val e: Throwable) : SearchResult()
object TerminalError : SearchResult()
