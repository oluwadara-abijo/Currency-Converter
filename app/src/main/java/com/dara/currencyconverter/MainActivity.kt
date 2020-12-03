package com.dara.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var rates: ArrayList<Rate>
    private lateinit var amount: String
    private lateinit var targetCurrency: String
    private lateinit var targetCurrencyListener: AdapterView.OnItemSelectedListener
    private lateinit var baseAmountWatcher: TextWatcher
    private lateinit var targetAmountWatcher: TextWatcher
    private var isReversed = false
    private lateinit var nairaRates: ArrayList<Float>

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
        nairaRates = arrayListOf()

        getRates()
        setGraphData()
        observeAmountInput()
        btn_convert.setOnClickListener { convert() }

    }

    private fun getRates() {
        loading_indicator.visibility = View.VISIBLE
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
                loading_indicator.visibility = View.GONE
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

    private fun setGraphData() {
        val values = ArrayList<Entry>()
        for (x in 1..7) {
            for (rate in nairaRates) {
                values.add(Entry(x.toFloat(), rate))
            }
        }

        // Create data entries using EUR to NGN rates
        val e1 = Entry(0f, 452.09f)
        val e2 = Entry(1f, 450.56f)
        val e3 = Entry(2f, 453.96f)
        val e4 = Entry(3f, 454.10f)
        val e5 = Entry(4f, 454.17f)
        val e6 = Entry(5f, 455.20f)
        val e7 = Entry(6f, 455.23f)
        values.addAll(listOf(e1, e2, e3, e4, e5, e6, e7))

        // Create a data set and give it a type
        val dataSet = LineDataSet(values, "")
        dataSet.apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            color = R.color.colorGraph
        }

        // Create a data object with the data sets
        val data = LineData(dataSet)

        // Customize chart
        graph.apply {
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                val labels = listOf("22 Nov", "23 Nov", "24 Nov", "25 Nov", "26 Nov", "27 Nov", "28 Nov")
                valueFormatter = IndexAxisValueFormatter(labels)
            }

            axisLeft.apply {
                setDrawGridLines(false)
                setDrawLabels(false)
                axisLineColor = android.R.color.transparent
            }

            axisRight.apply {
                setDrawGridLines(false)
                setDrawLabels(false)
                axisLineColor = android.R.color.transparent
            }
        }

        // Set data
        graph.data = data

    }

}