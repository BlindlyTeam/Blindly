package ch.epfl.sdp.blindly.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationPermission {

    var coarseGrainPermission = false
    var fineGrainPermission = false

    var permission = false

    private fun checkLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission(activity: Activity) {
        coarseGrainPermission = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
        fineGrainPermission = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun checkAndRequestLocationPermission(context: Context, activity: Activity) {
        if(checkLocationPermission(context)) {
            requestLocationPermission(activity)
            permission = coarseGrainPermission && fineGrainPermission
        }
    }


}