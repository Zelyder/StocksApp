package com.zelyder.stocksapp.data.storage

object DbContract {
    const val DATABASE_NAME = "Stocks.db"

    object Stocks {
        const val TABLE_NAME = "stocks"

        const val COLUMN_NAME_TICKER = "ticker"
        const val COLUMN_NAME_COMPANY_NAME = "companyName"
        const val COLUMN_NAME_LOGO = "logo"
        const val COLUMN_NAME_PRICE = "price"
        const val COLUMN_NAME_CURRENCY = "currency"
        const val COLUMN_NAME_DAY_DELTA = "dayDelta"
        const val COLUMN_NAME_DAY_DELTA_PERCENT = "dayDeltaPercent"
        const val COLUMN_NAME_FAVORITE = "isFavorite"
    }

    object Favorites {
        const val  TABLE_NAME = "favorite"
    }

}