package ch.epfl.sdp.blindly.main_screen.match.cards

import android.os.Parcel
import android.os.Parcelable

/**
 * Class to have the data to display to the matchActivity
 * We need it to be parcelable to be able to pass them between different activities
 *
 * @property name
 * @property age
 * @property gender
 * @property distance
 * @property recordingPath
 */
data class Profile(
    val uid: String,
    val name: String,
    val age: Int,
    val gender: String,
    val distance: Int,
    val recordingPath: String,
) : Parcelable {
    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Profile> = object : Parcelable.Creator<Profile> {
            override fun createFromParcel(source: Parcel?): Profile {
                return Profile(source!!)
            }

            override fun newArray(size: Int): Array<Profile?> {
                return arrayOfNulls(size)
            }
        }
    }

    /**
     * A constructor for a profile from a parcel
     */
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readInt(),
        source.readString()!!,
        source.readInt(),
        source.readString()!!
    )

    /**
     * Object doesn't contain a content descriptor
     *
     * @return contains content description (no)
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * Create a parcel into dest
     *
     * @param dest the destination where the parcel is writen to
     * @param flags error flag
     */
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(uid)
        dest?.writeString(name)
        dest?.writeInt(age)
        dest?.writeString(gender)
        dest?.writeInt(distance)
        dest?.writeString(recordingPath)
    }

    override fun toString(): String {
        return "$name, $age, $gender"
    }
}
