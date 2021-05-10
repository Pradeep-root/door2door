package com.pradeep.door2door.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pradeep.door2door.data.network.ConnectivityInterceptor

class ConnectionReceiver : BroadcastReceiver() {

    private val _connectivityMutableLiveData = MutableLiveData<Boolean>()
    val connectivityMutableLiveData : LiveData<Boolean> = _connectivityMutableLiveData


    override fun onReceive(context: Context?, intent: Intent?) {
        _connectivityMutableLiveData.postValue(ConnectivityInterceptor(context).isInternetConnection())
    }
}