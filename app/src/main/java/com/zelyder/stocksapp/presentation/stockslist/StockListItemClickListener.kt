package com.zelyder.stocksapp.presentation.stockslist

import com.zelyder.stocksapp.domain.models.Stock

interface StockListItemClickListener {
    fun onClickFavourite(stock: Stock)
}