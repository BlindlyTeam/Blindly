package ch.epfl.sdp.blindly.location

import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng


// Simple wrapper to let firebase handle it nicely
class BlindlyLatLng : Parcelable {
    private var latitude: Double? = null
    private var longitude: Double? = null

    constructor(parcel: Parcel) : this() {
        latitude = parcel.readValue(Double::class.java.classLoader) as? Double
        longitude = parcel.readValue(Double::class.java.classLoader) as? Double
    }

    @Suppress("unused") // needed for firebase
    fun getLatitude(): Double? {
        return latitude
    }

    @Suppress("unused") // needed for firebase
    fun getLongitude(): Double? {
        return longitude
    }

    @Suppress("unused") // needed for firebase
    constructor() {
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BlindlyLatLng> {
        override fun createFromParcel(parcel: Parcel): BlindlyLatLng {
            return BlindlyLatLng(parcel)
        }

        override fun newArray(size: Int): Array<BlindlyLatLng?> {
            return arrayOfNulls(size)
        }
    }
}
