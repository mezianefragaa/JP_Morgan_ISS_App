package com.example.jpmorganissapp.util

import android.location.Location
import com.example.jpmorganissapp.domain.model.AstrosModel
import com.example.jpmorganissapp.domain.model.IssModel
import com.google.android.gms.maps.model.LatLng

fun IssModel.toLocation():Location{
   return Location("").apply {
        latitude = this@toLocation.latitude
        longitude = this@toLocation.longitude
    }
}
fun IssModel.toLatlng(): LatLng{
    return LatLng(this.latitude,this.longitude)
}
fun Location.toLatLng() : LatLng{
    return  LatLng(this.latitude, this.longitude)
}

