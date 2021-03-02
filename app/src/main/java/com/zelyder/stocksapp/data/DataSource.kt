package com.zelyder.stocksapp.data

import com.zelyder.stocksapp.domain.models.Stock

class DataSource {

    companion object {
        fun getStocks(): List<Stock> = listOf(
            Stock(
                "YNDX",
                "Yandex, LLC",
                "https://finnhub.io/api/logo?symbol=YNDX",
                4764.6f,
                "ru",
                55.0f,
                1.15f
            ),
            Stock(
                "AAPL",
                "Apple Inc.",
                "https://finnhub.io/api/logo?symbol=AAPL",
                131.93f,
                "eng",
                0.12f,
                1.15f,
                true
            ),
            Stock(
                "GOOGL",
                "Alphabet Class A",
                "${LOGO_BASE_URL}GOOGL",
                1802f,
                "eng",
                0.12f,
                1.15f
            ),
            Stock(
                "AMZN",
                "Amazon.com",
                "https://finnhub.io/api/logo?symbol=AMZN",
                3204f,
                "eng",
                -0.12f,
                1.15f
            ),
            Stock(
                "BAC",
                "Bank of America Corp",
                "https://finnhub.io/api/logo?symbol=BAC",
                3204f,
                "eng",
                0.12f,
                1.15f
            ),
        )
    }
}