package com.zelyder.stocksapp.presentation.searchscreen

sealed class SearchState
object Loading : SearchState()
object Ready : SearchState()