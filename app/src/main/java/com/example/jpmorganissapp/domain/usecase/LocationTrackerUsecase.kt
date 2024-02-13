package com.example.jpmorganissapp.domain.usecase

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * LocationTracker to retrieve last user location using a call back
 */
@Singleton
class LocationTrackerUseCase @Inject constructor(
    private val client: FusedLocationProviderClient) {
    @SuppressLint("MissingPermission")
    @ExperimentalCoroutinesApi
    fun execute(): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(UPDATE_INTERVAL_SECS)).apply {
            setMinUpdateDistanceMeters(MIN_UPDATE_DISTANCE_METERS)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation

                location?.let {
                    trySend(it)
                }
            }
        }

        client.requestLocationUpdates(locationRequest, callBack, Looper.getMainLooper())
        awaitClose { client.removeLocationUpdates(callBack) }
    }

    companion object {
        private const val UPDATE_INTERVAL_SECS = 10L
        private const val MIN_UPDATE_DISTANCE_METERS = 10f
        private const val FASTEST_UPDATE_INTERVAL_SECS = 2L
    }
}