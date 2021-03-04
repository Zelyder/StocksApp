package com.zelyder.stocksapp.presentation.stockslist

interface StockListItemClickListener {
    fun onClickFavourite(ticker: String, isFavorite: Boolean)
}