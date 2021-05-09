package com.pradeep.door2door.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.backbase.assignment.util.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.pradeep.door2door.R
import com.pradeep.door2door.data.models.BookingInfo
import com.pradeep.door2door.utils.AnimationUtils
import com.pradeep.door2door.utils.MapUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_maps.*


@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var  viewModel : MapsViewModel
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)

        setupBookingInfoObserver()
    }

    private fun setupBookingInfoObserver() {

        viewModel.bookingInfoLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data.let { bookingInfo ->
                        val latitude = bookingInfo?.data?.lat
                        val longitude = bookingInfo?.data?.lng

                        if (latitude != null && longitude != null) {
                            showMovingCab(LatLng(latitude, longitude))
                        }

                       bookingInfo?.let {
                           updateBookingInfo(bookingInfo)
                       }
                    }
                }

                Status.ERROR -> {

                }

                Status.LOADING -> {
                    //
                }
            }
        })
    }

    private fun updateBookingInfo(bookingInfo: BookingInfo) {
        val pickupAddress = bookingInfo.data?.pickupLocation?.address
        val dropAddress = bookingInfo.data?.dropoffLocation?.address
        val status = bookingInfo.data?.status
        if(pickupAddress != null && dropAddress != null){
            tv_source.text = pickupAddress
            tv_destination.text = dropAddress
            tv_vehicle_status.text = status
        }
    }

    private fun showMovingCab(latLng: LatLng) {
        updateCarLocation(latLng)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val berlin = LatLng(13.416967391967772, 52.521992025025575)
        showDefaultLocationOnMap(berlin)
        viewModel.getBookingInfo()
    }

    private fun moveCamera(latLng: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15.5f).build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
       // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun showDefaultLocationOnMap(latLng: LatLng) {
        moveCamera(latLng)
        animateCamera(latLng)
    }

   private fun getCarBitmap(context: Context): Bitmap {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_car)
        return Bitmap.createScaledBitmap(bitmap, 60, 110, false)
    }

    private fun addCarMarkerAndGet(latLng: LatLng): Marker {
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getCarBitmap(this))
        return mMap.addMarker(
                MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor)
        )
    }


    private var movingCabMarker: Marker? = null
    private var previousLatLng: LatLng? = null
    private var currentLatLng: LatLng? = null


    private fun updateCarLocation(latLng: LatLng) {
        if (movingCabMarker == null) {
            movingCabMarker = addCarMarkerAndGet(latLng)
        }
        if (previousLatLng == null) {
            currentLatLng = latLng
            previousLatLng = currentLatLng
            movingCabMarker?.position = currentLatLng
            movingCabMarker?.setAnchor(0.5f, 0.5f)
            animateCamera(currentLatLng!!)
        } else {
            previousLatLng = currentLatLng
            currentLatLng = latLng
            val valueAnimator = AnimationUtils.carAnimator()
            valueAnimator.addUpdateListener { va ->
                if (currentLatLng != null && previousLatLng != null) {
                    val multiplier = va.animatedFraction
                    val nextLocation = LatLng(
                            multiplier * currentLatLng!!.latitude + (1 - multiplier) * previousLatLng!!.latitude,
                            multiplier * currentLatLng!!.longitude + (1 - multiplier) * previousLatLng!!.longitude
                    )
                    movingCabMarker?.position = nextLocation
                    val rotation = MapUtils.getRotation(previousLatLng!!, nextLocation)
                    if (!rotation.isNaN()) {
                        movingCabMarker?.rotation = rotation
                    }
                    movingCabMarker?.setAnchor(0.5f, 0.5f)
                    animateCamera(nextLocation)
                }
            }
            valueAnimator.start()
        }
    }

}