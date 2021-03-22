package com.zelyder.stocksapp.presentation.core

import android.content.res.Resources
import com.zelyder.stocksapp.R
import kotlin.math.abs

fun toDeltaString(
    delta: Float,
    deltaPercent: Float,
    currency: String,
    resources: Resources
): String {

    val deltaTemplate: Int = when (currency) {
        "ru" -> {
            R.string.delta_ru
        }
        "eng" -> {
            R.string.delta_eng
        }
        else -> {
            R.string.delta_eng
        }
    }
    var strDelta: String = resources.getString(deltaTemplate, abs(delta), abs(deltaPercent))
    strDelta = if (delta < 0) {
        "-$strDelta"
    } else {
        "+$strDelta"
    }
    return strDelta
}

fun toPriceString(
    price: Float,
    currency: String,
    resources: Resources
): String {

    val currencyTemplate: Int = when (currency) {
        "ru" -> {
            R.string.currency_ru
        }
        "eng" -> {
            R.string.currency_eng
        }
        else -> {
            R.string.currency_eng
        }
    }
    return resources.getString(currencyTemplate, price)

}



