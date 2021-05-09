package com.pradeep.door2door.repository

import android.annotation.SuppressLint
import com.pradeep.door2door.data.models.BookingInfo
import com.pradeep.door2door.data.network.Door2DoorSocket
import io.reactivex.Flowable
import javax.inject.Inject

class Door2DoorRepository @Inject constructor(private val doorSocket: Door2DoorSocket) : Repository {


    @SuppressLint("CheckResult")
    override fun getBookingInfo(): Flowable<BookingInfo> {
       return doorSocket.observeSocket()
    }
}