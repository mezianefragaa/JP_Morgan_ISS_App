package com.example.jpmorganissapp.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.core.content.ContextCompat
import com.example.jpmorganissapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * addMarker is an extension function that handles both markers and marker onclick listener
 */
fun getMapMarkerCallback(location: LatLng,
                         context: Context,
                         satellite: Int,
                         dialogMessage: String?,
                         btnOk: Int?): OnMapReadyCallback {
   return OnMapReadyCallback { googleMap ->
        googleMap.clear()

        googleMap.addMarker(
            MarkerOptions().position(location).icon(
                convertRtoBitmap(
                    context,
                    satellite
                )
            )
        )
        val cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(0f)
            .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap.cameraPosition.target.latitude
        googleMap.setOnMarkerClickListener {
            if (dialogMessage?.isNotEmpty()==true){
                val alertDialog = AlertDialog.Builder(context)
                    .setTitle(ContextCompat.getString(context, R.string.crew))
                    .setMessage(dialogMessage)
                    .setPositiveButton(btnOk?.let { it1 ->
                        ContextCompat.getString(
                            context,
                            it1
                        )
                    }, DialogInterface.OnClickListener { _, _ ->  })
                alertDialog.show()
                true
        }
            else false
        }
    }
}
