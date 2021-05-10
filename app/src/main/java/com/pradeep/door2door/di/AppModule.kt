package com.pradeep.door2door.di

import android.content.Context
import com.pradeep.door2door.data.network.ConnectivityInterceptor
import com.pradeep.door2door.data.network.Door2DoorSocket
import com.pradeep.door2door.repository.Door2DoorRepository
import com.pradeep.door2door.repository.Repository
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun okhttpProvides(@ApplicationContext context : Context) = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun scarletProvides(okHttpClient: OkHttpClient) = Scarlet.Builder()
        .webSocketFactory(okHttpClient.newWebSocketFactory("wss://d2d-frontend-code-challenge.herokuapp.com"))
        .addMessageAdapterFactory(GsonMessageAdapter.Factory())
        .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun door2doorSocketProvides(scarlet: Scarlet) = scarlet.create<Door2DoorSocket>()

    fun repositoryProvides(door2DoorSocket: Door2DoorSocket) = Door2DoorRepository(door2DoorSocket) as Repository

}