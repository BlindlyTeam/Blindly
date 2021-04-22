package ch.epfl.sdp.blindly.match

import android.os.Parcel
import android.os.Parcelable

/**
 * Class to have the data to display to the matchActivity
 * We need it to be parcelable to be able to pass them between different activities
 *
 * @property name
 * @property age
 */
data class Profile(
    val name: String,
    val age: Int,
) : Parcelable {
    companion object {
        private var counter = 0L

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
    constructor(source: Parcel) : this(source.readString()!!, source.readInt())

    /**
     * Describe the content as in Integer
     *
     * @return content description (0)
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
        dest?.writeString(name)
        dest?.writeInt(age)
    }

    override fun toString(): String {
        return "$name, $age"
    }
}
