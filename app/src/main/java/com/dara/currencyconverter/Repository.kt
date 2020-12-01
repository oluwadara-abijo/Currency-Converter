package com.dara.currencyconverter

class Repository {

    private val service = FixerClient.getService()

    suspend fun getCurrencies() = service.getCurrencies()
}