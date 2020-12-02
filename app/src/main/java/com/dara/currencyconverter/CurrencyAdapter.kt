package com.dara.currencyconverter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CurrencyAdapter(private val rates: List<Rate>) :
    BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_currency, parent, false)
        val textViewBankName = view.findViewById<TextView>(R.id.tv_currency)
        textViewBankName.text = rates[position].currency
        return view
    }

    override fun getCount(): Int {
        return rates.size
    }

    override fun getItem(position: Int): String {
        return rates[position].currency
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

}