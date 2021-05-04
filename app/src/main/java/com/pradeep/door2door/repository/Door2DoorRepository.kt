package com.pradeep.door2door.repository

import android.annotation.SuppressLint
import android.util.Log
import com.pradeep.door2door.data.Door2DoorSocket
import javax.inject.Inject

class Door2DoorRepository @Inject constructor(private val doorSocket: Door2DoorSocket) {


    @SuppressLint("CheckResult")
    fun getData(){
        doorSocket.observeNews()
            .subscribe { bookingInfo ->
                Log.d("Data", bookingInfo.toString())
            }
    }

}