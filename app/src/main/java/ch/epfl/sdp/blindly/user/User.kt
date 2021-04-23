package ch.epfl.sdp.blindly.user

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.Period

/**
 * A class used to represent a user
 */
@Serializable
class User private constructor(
    val username: String?,
    val location: String?,
    val birthday: String?,
    val gender: String?,
    val sexualOrientations: List<String>,
    val showMe: String?,
    val passions: List<String>,
    val radius: Int?,
    val matches: List<User>,
    val description: String?,
    val ageRange: List<Int>
) {

    /**
     * A builder used to partially initialize a user during the profile_setup activities
     * Made serializable so that it can be put in a bundle and passed as extrax
     */
    @Serializable
    data class Builder(
        var username: String? = null,
        var location: String? = null,
        var birthday: String? = null,
        var gender: String? = null,
        var sexualOrientations: List<String> = listOf(),
        var showMe: String? = null,
        var passions: List<String> = listOf(),
        var radius: Int? = null,
        var matches: List<User> = listOf(),
        var description: String? = null,
        var ageRange: List<Int> = listOf()
    ) {

        fun setUsername(username: String) = apply {
            this.username = username
        }

        fun setLocation(location: String) = apply {
            this.location = location
        }

        fun setBirthday(birthday: String) = apply {
            this.birthday = birthday
        }

        fun setGender(gender: String) = apply {
            this.gender = gender
        }

        fun setSexualOrientations(sexual_orientations: List<String>) = apply {
            this.sexualOrientations = sexual_orientations
        }

        fun setShowMe(showMe: String) = apply {
            this.showMe = showMe
        }

        fun setPassions(passions: List<String>) = apply {
            this.passions = passions
        }

        fun setRadius(radius: Int) = apply {
            this.radius = radius
        }

        fun setMatches(matches: List<User>) = apply {
            this.matches = matches
        }

        fun setDescription(description: String) = apply {
            this.description = description
        }

        fun setAgeRange(ageRange: List<Int>) = apply {
            this.ageRange = ageRange
        }

        fun build(): User {
            return User(
                username,
                location,
                birthday,
                gender,
                sexualOrientations,
                showMe,
                passions,
                radius,
                matches,
                description,
                ageRange
            )
        }

    }

    companion object {
        private const val TAG = "User"

        /**
         * Converts the document received from firestore back into a User
         */
        fun DocumentSnapshot.toUser(): User? {
            try {
                val username = getString("username")!!
                val location = getString("location")!!
                val birthday = getString("birthday")!!
                val gender = getString("gender")!!
                val sexualOrientations = get("sexualOrientations") as List<String>
                val showMe = getString("showMe")!!
                val passions = get("passions") as List<String>
                val radius = getField<Int>("radius")!!
                val matches = get("matches") as List<User>
                val description = getString("description")!!
                val ageRange = get("ageRange") as List<Int>
                return User(
                    username,
                    location,
                    birthday,
                    gender,
                    sexualOrientations,
                    showMe,
                    passions,
                    radius,
                    matches,
                    description,
                    ageRange
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                return null
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getUserAge(user: User?): Int? {
            val birthday = user?.birthday
            if (birthday != null) {
                return getAgeFromBirthday(birthday)
            }
            return null
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getAgeFromBirthday(birthday: String): Int {
            val (day, month, year) = birthday.split('.')
            return Period.between(
                LocalDate.of(
                    year.toInt(),
                    month.toInt(),
                    day.toInt()
                ),
                LocalDate.now()
            ).years
        }
    }

}
