package ch.epfl.sdp.blindly.utils

import java.net.InetAddress

class CheckInternet {

    companion object {
        fun internetIsConnected(): Boolean {
            return try {
                val ipAddr: InetAddress = InetAddress.getByName("google.com")
                !ipAddr.equals("")
            } catch (e: Exception) {
                false
            }
        }
    }
}