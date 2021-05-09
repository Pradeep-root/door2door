package com.pradeep.door2door.repository

import com.pradeep.door2door.data.models.BookingInfo
import io.reactivex.Flowable

interface Repository {

    fun getBookingInfo() : Flowable<BookingInfo>

}