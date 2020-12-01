package com.dara.currencyconverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {

    private val repository = Repository()

    val currencies: LiveData<SymbolsResponse?> = liveData(Dispatchers.IO) {
        try {
            val result = repository.getCurrencies()
            emit(result)
        } catch (e : CancellationException) {
            emit(null)
        }
    }
}