package xyz.tetatet.vivaquiz.io.interceptors

import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class CheckConnectivityInterceptor(private val connMan: ConnectivityManager) : Interceptor {

    /*Inner Functions -----------------------------------------------*/

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val netInfo = connMan.activeNetworkInfo
        if (netInfo == null || !netInfo.isConnected) {
            throw NoNetworkException()
        }
        return chain.proceed(chain.request())
    }
    /*Custom Functions/Class -----------------------------------------------*/
    inner class NoNetworkException : IOException()
}