package ch.epfl.sdp.blindly.location

import android.location.Location
import com.google.android.gms.maps.model.LatLng


// Simple wrapper to let firebase handle it nicely
class BlindlyLatLng {
    private var latitude: Double? = null
    private var longitude: Double? = null

    @Suppress("unused") // needed for firebase
    fun getLatitude(): Double? {
        return latitude
    }

    @Suppress("unused") // needed for firebase
    fun getLongitude(): Double? {
        return longitude
    }

    @Suppress("unused") // needed for firebase
    constructor() {}

    constructor(latitude: Double?, longitude: Double?) {
        this.latitude = latitude
        this.longitude = longitude
    }
    constructor(latLng: LatLng) {
        latitude = latLng.latitude
        longitude = latLng.longitude
    }

    constructor(latLng: Location?) {
        this.latitude = latLng?.latitude
        this.longitude = latLng?.longitude
    }

    fun toLatLng(): LatLng? {
        latitude ?: return null
        longitude ?: return null
        return LatLng(latitude!!, longitude!!)
    }
}
