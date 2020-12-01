package com.dara.currencyconverter

import retrofit2.http.GET

interface FixerService {

    @GET("symbols")
    suspend fun getCurrencies(): Any

}
