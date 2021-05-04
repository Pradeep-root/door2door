package com.pradeep.door2door

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Door2DoorApplication : Application(){

    override fun onCreate() {
        super.onCreate()
    }
}