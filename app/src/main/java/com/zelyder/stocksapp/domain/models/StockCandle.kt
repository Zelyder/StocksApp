package com.zelyder.stocksapp.domain.models

data class StockCandle(
    val closePrices: List<Float>,
    val statusOfResponse: String,
    val timeStamps: List<Long>,
)
