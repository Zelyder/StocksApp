package com.zelyder.stocksapp.presentation.core

import android.content.res.Resources
import com.zelyder.stocksapp.R
import kotlinx.datetime.Instant.Companion.fromEpochMilliseconds
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toLocalDateTime
import java.time.Instant
import java.time.format.TextStyle
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

fun Long.toDate():String {
    val instant = fromEpochMilliseconds(this)
    val dateTime = instant.toLocalDateTime(currentSystemDefault())
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        "${dateTime.dayOfMonth} ${dateTime.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} ${dateTime.year}"
    } else {
        "${dateTime.dayOfMonth} ${dateTime.month.name} ${dateTime.year}"
    }
}


