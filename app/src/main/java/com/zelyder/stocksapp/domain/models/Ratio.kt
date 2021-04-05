package com.zelyder.stocksapp.domain.models

import kotlinx.serialization.SerialName

data class Ratio(
    var name: String = "",
    var description: String = "",
    var value: Double
)
