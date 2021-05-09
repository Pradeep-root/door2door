package com.pradeep.door2door.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.backbase.assignment.util.Resource
import com.pradeep.door2door.data.models.BookingInfo
import com.pradeep.door2door.repository.Door2DoorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Scheduler
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: Door2DoorRepository) : ViewModel() {

    val bookingInfoLiveData = MutableLiveData<Resource<BookingInfo>>()

    fun getBookingInfo(){
       // bookingInfoLiveData.postValue(Resource.loading())
       try {
           repository.getBookingInfo()
               .subscribe{
               bookingInfoLiveData.postValue(Resource.success(it))
           }
       }catch (ex : IOException){
           bookingInfoLiveData.postValue(Resource.error(ex.message.toString(), null))
       }
    }

}