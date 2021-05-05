package ch.epfl.sdp.blindly.helpers

import android.R.attr.author
import com.google.android.gms.maps.model.LatLng


// Simple wrapper to let firebase handle it nicely
class BlindlyLatLng {
    private var latitude: Double? = null
    private var longitude: Double? = null

    fun getLatitude(): Double? {
        return latitude
    }

    fun getLongitude(): Double? {
        return longitude
    }

    public constructor() {}

    public constructor(latitude: Double?, longitude: Double?) {
        this.latitude = latitude
        this.longitude = longitude
    }
    constructor(latLng: LatLng) {
        latitude = latLng.latitude
        longitude = latLng.longitude
    }

    fun toLatLng(): LatLng? {
        latitude ?: return null
        longitude ?: return return null
        return LatLng(latitude!!, longitude!!)
    }
}
