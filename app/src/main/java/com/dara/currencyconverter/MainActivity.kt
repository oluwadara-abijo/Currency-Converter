package com.dara.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCurrencies()
    }

    private fun getCurrencies() {
        viewModel.currencies.observe(this, Observer {
            if (it != null) {
                val currencySymbols = it.symbols.keys
                val currencyAdapter = CurrencyAdapter(currencySymbols.toList())
                spinner_from.adapter = currencyAdapter
                spinner_to.adapter = currencyAdapter
            }
        })
    }
}