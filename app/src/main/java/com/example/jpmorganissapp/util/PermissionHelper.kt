package com.example.jpmorganissapp.util

import android.Manifest

 fun locationPermissionsGranted(permissions: Map<String, Boolean>?): Boolean {
    if (permissions == null) return false
    return permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
}