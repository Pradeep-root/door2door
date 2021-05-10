package com.pradeep.door2door.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import com.pradeep.door2door.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(val context: Context?) : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if(!isInternetConnection()){
           throw NoNetworkException()
        }
        return chain.proceed(originalRequest)
    }


     fun isInternetConnection(): Boolean{
        val connectivityManager = context?.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return  when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }

        }else{
            // for lower end devices
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    inner class NoNetworkException internal constructor() : IOException(context?.getString(R.string.no_internet_connection))
}