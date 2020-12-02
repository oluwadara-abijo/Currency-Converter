package com.dara.currencyconverter

import retrofit2.http.GET

interface FixerService {

    @GET("latest")
    suspend fun getRates(): RatesResponse

}
