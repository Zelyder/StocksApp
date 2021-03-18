package com.zelyder.stocksapp.data.mappers

import com.zelyder.stocksapp.data.LOGO_BASE_URL
import com.zelyder.stocksapp.data.network.dto.finnhub.FoundStockDto
import com.zelyder.stocksapp.data.network.dto.finnhub.StockInfoDto
import com.zelyder.stocksapp.data.network.dto.finnhub.StockPriceDto
import com.zelyder.stocksapp.data.network.dto.mboum.MostActivesStockDto
import com.zelyder.stocksapp.data.storage.entities.FavoriteEntity
import com.zelyder.stocksapp.data.storage.entities.StockEntity
import com.zelyder.stocksapp.domain.models.Stock
import kotlin.math.abs
import kotlin.math.round

fun StockEntity.toStock() = Stock(
        ticker = this.ticker,
        companyName = this.companyName,
        logo = this.logo,
        price = this.price,
        currency = this.currency,
        dayDelta = this.dayDelta,
        dayDeltaPercent = this.dayDeltaPercent,
        isFavorite = this.isFavorite
)

fun MostActivesStockDto.toStock() = Stock(
        ticker = this.ticker,
        logo = "$LOGO_BASE_URL${this.ticker}",
        companyName = this.shortName,
        price = this.regularMarketPrice,
        currency = this.currency,
        dayDelta = round(this.regularMarketChange * 100.0f) / 100.0f,
        dayDeltaPercent = this.regularMarketChangePercent
)

fun Stock.toEntity() = StockEntity(
        ticker = this.ticker,
        companyName = this.companyName,
        logo = this.logo,
        price = this.price,
        currency = this.currency,
        dayDelta = this.dayDelta,
        dayDeltaPercent = this.dayDeltaPercent,
        isFavorite = this.isFavorite
)

fun Stock.toFavoriteStock() = FavoriteEntity(
        ticker = this.ticker,
        companyName = this.companyName,
        logo = this.logo,
        price = this.price,
        currency = this.currency,
        dayDelta = this.dayDelta,
        dayDeltaPercent = this.dayDeltaPercent,
        isFavorite = this.isFavorite
)

fun StockEntity.toFavoriteStock() = FavoriteEntity(
        ticker = this.ticker,
        companyName = this.companyName,
        logo = this.logo,
        price = this.price,
        currency = this.currency,
        dayDelta = this.dayDelta,
        dayDeltaPercent = this.dayDeltaPercent,
        isFavorite = this.isFavorite
)

fun FavoriteEntity.toStockEntity() = StockEntity(
        ticker = this.ticker,
        companyName = this.companyName,
        logo = this.logo,
        price = this.price,
        currency = this.currency,
        dayDelta = this.dayDelta,
        dayDeltaPercent = this.dayDeltaPercent,
        isFavorite = this.isFavorite
)

fun FavoriteEntity.toStock() = Stock(
        ticker = this.ticker,
        companyName = this.companyName,
        logo = this.logo,
        price = this.price,
        currency = this.currency,
        dayDelta = this.dayDelta,
        dayDeltaPercent = this.dayDeltaPercent,
        isFavorite = this.isFavorite
)
//FIXME: missed currency
fun FoundStockDto.toStock(priceDto: StockPriceDto, isFavorite: Boolean):Stock {
        val delta = priceDto.current - priceDto.previousClose
        return Stock(
                ticker = symbol,
                companyName = description,
                logo = "$LOGO_BASE_URL${symbol}",
                price = priceDto.current,
                dayDelta = delta,
                dayDeltaPercent = abs(delta*100.0f) / priceDto.previousClose,
                isFavorite = isFavorite
        )
}