package com.pradeep.door2door.data.network

import com.pradeep.door2door.data.models.BookingInfo
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import io.reactivex.Flowable


interface Door2DoorSocket {

    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>

    @Receive
    fun observeSocket(): Flowable<BookingInfo>

}