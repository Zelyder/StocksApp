package com.zelyder.stocksapp.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Stock (
    val ticker: String = "",
    val companyName: String = "",
    val logo: String = "",
    var price: Float = 0.0f,
    val currency: String = "",
    var dayDelta: Float = 0.0f,
    var dayDeltaPercent: Float = 0.0f,
    var isFavorite: Boolean = false
): Parcelable