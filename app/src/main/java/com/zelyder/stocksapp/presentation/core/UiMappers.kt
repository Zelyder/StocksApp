package com.zelyder.stocksapp.presentation.core

import android.content.res.Resources
import com.zelyder.stocksapp.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
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

fun Long.toDate(showTime: Boolean = false): String {
    return if(showTime) {
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy\n HH:mm", Locale.getDefault())
        simpleDateFormat.format(Date(this))

    }else {
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        simpleDateFormat.format(Date(this))
    }
}


