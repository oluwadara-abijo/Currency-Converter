package com.dara.currencyconverter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CurrencyAdapter(private val currencies: List<String>) :
    BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_currency, parent, false)
        val textViewBankName = view.findViewById<TextView>(R.id.tv_currency)
        textViewBankName.text = currencies[position]
        return view
    }

    override fun getCount(): Int {
        return currencies.size
    }

    override fun getItem(position: Int): String {
        return currencies[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

}