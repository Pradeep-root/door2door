package com.pradeep.door2door.data

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import io.reactivex.Flowable


interface Door2DoorSocket {

    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>

    @Receive
    fun observeNews(): Flowable<BookingInfo>
}