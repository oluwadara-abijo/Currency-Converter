package com.dara.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var rates: ArrayList<Rate>
    private lateinit var amount: String
    private lateinit var targetCurrency: String
    private lateinit var targetCurrencyListener: AdapterView.OnItemSelectedListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner_from.isEnabled = false
        rates = arrayListOf()
        getRates()

        targetCurrencyListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                targetCurrency = rates[position].currency
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        btn_convert.setOnClickListener { convert() }

    }

    private fun getRates() {
        viewModel.rates.observe(this, {
            if (it != null) {
                // Create a list of [Rate] objects from the response
                it.rates.forEach { (k, v) ->
                    val rate = Rate(k, v)
                    rates.add(rate)
                }

                targetCurrency = rates[0].currency

                // Setup spinners
                val currencyAdapter = CurrencyAdapter(rates)
                spinner_from.apply {
                    adapter = currencyAdapter
                    setSelection(46)
                    tv_currency_from.text = adapter.getItem(46) as String
                }

                spinner_to.apply {
                    adapter = currencyAdapter
                    onItemSelectedListener = targetCurrencyListener
                    tv_currency_to.text = adapter.getItem(0) as String
                }
            }
        })
    }

    private fun convert() {
        amount = edit_amt_from.text.toString()
        if (amount.isNotEmpty()) {
            val rate = rates.first { it.currency == targetCurrency }.rate
            val result = amount.toFloat() * rate
            edit_amt_to.setText(result.toString())
        } else {
            Toast.makeText(this, "Enter an amount", Toast.LENGTH_SHORT).show()
        }
    }
}