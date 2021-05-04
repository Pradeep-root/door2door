package com.pradeep.door2door.ui

import androidx.lifecycle.ViewModel
import com.pradeep.door2door.repository.Door2DoorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: Door2DoorRepository) : ViewModel() {


    fun getSocket(){
        repository.getData()
    }


}