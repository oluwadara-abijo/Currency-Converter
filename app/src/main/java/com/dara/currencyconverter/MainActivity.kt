package com.dara.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private lateinit var baseAmountWatcher: TextWatcher
    private lateinit var targetAmountWatcher: TextWatcher
    private var isReversed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        targetCurrencyListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                targetCurrency = rates[position].currency
                tv_currency_to.text = targetCurrency
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spinner_from.isEnabled = false
        rates = arrayListOf()

        getRates()
        observeAmountInput()
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
        if (amount.isNotEmpty()) {
            val rate = rates.first { it.currency == targetCurrency }.rate
            if (isReversed) {
                val result = amount.toFloat() / rate
                edit_amt_from.setText(result.toString())
            } else {
                val result = amount.toFloat() * rate
                edit_amt_to.setText(result.toString())
            }
        } else {
            Toast.makeText(this, "Enter an amount", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Observes amount inputs and allows conversion both ways, depending on the amount input
     * field that was changed last
     */
    private fun observeAmountInput() {
        // Initialize text watchers to observe amount inputs
        baseAmountWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                isReversed = false
                amount = edit_amt_from.text.toString()
            }
        }
        edit_amt_from.addTextChangedListener(baseAmountWatcher)

        targetAmountWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                isReversed = true
                amount = edit_amt_to.text.toString()

            }
        }
        edit_amt_to.addTextChangedListener(targetAmountWatcher)
    }

}