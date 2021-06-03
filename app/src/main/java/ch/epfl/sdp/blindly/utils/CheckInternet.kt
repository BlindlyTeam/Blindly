package ch.epfl.sdp.blindly.utils

import android.content.Context
import android.net.ConnectivityManager

class CheckInternet {

    companion object {
        const val TAG = "CheckInternet"

        fun internetIsConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetwork != null
        }
    }
}