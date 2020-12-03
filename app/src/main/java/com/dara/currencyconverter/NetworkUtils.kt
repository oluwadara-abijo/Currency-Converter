package com.dara.currencyconverter

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class NetworkUtils(
    private val activity: AppCompatActivity,
    private val progressBar: View?
) {

    /**
     * This function checks for network connectivity
     */
    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    /**
     * This displays the progress bar when app is communicating with the server
     */
    fun showLoading() {
        progressBar?.visibility = View.VISIBLE
        activity.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    /**
     * This hides the progress bar
     */
    fun hideLoading() {
        progressBar?.visibility = View.GONE
        activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}