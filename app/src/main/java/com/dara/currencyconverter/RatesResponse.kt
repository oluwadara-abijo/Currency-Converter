package com.dara.currencyconverter

data class RatesResponse(
    val success: Boolean,
    val timeStamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Float>
) {
}