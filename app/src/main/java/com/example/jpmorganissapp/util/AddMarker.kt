package com.example.jpmorganissapp.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.jpmorganissapp.R
import com.example.jpmorganissapp.domain.extentions.convertRtoBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * addMarker is an extension function that handles both markers and marker onclick listener
 */
fun addMarker(location: LatLng, context: Context, childFragment: FragmentManager, satellite: Int, astrosModel: Any?, btnOk: Int?) {
    val callback = OnMapReadyCallback { googleMap ->
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
            if (btnOk != 0){
            val alertDialog = AlertDialog.Builder(context)
                .setTitle(ContextCompat.getString(context, R.string.crew))
                .setItems(listOf( astrosModel.toString()).toTypedArray(), DialogInterface.OnClickListener { dialog, which ->  })
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
    val mapFragment = childFragment.findFragmentById(R.id.map) as SupportMapFragment?
    mapFragment?.getMapAsync(callback)
}
