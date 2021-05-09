package com.pradeep.door2door.data.models

data class VehicleStatus(
    val lat : Double?,
    val lng : Double?,
    val address : String?,
    val pickupLocation : PickupLocation,
    val dropoffLocation : DropOffLocation,
    val status : String?
) {

    inner class PickupLocation(
        val lat : Double?,
        val lng : Double?,
        val address : String?
    )

    inner class DropOffLocation(
        val lat : Double?,
        val lng : Double?,
        val address : String?
    )

}