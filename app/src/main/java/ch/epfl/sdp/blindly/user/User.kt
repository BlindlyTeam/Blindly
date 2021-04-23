package ch.epfl.sdp.blindly.user

import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.Period

/**
 * A class used to represent a user
 */
@Serializable
class User private constructor(
    val username: String?,
    @Contextual val location: Location?,
    val birthday: String?,
    val gender: String?,
    val sexual_orientations: List<String>,
    val show_me: String?,
    val passions: List<String>,
    val radius: Int?,
    val matches: List<User>,
    val description: String?
) {

    /**
     * A builder used to partially initialize a user during the profile_setup activities
     * Made serializable so that it can be put in a bundle and passed as extrax
     */
    @Serializable
    data class Builder(
        var username: String? = null,
        @Contextual var location: Location? = null,
        var birthday: String? = null,
        var gender: String? = null,
        var sexual_orientations: List<String> = listOf(),
        var show_me: String? = null,
        var passions: List<String> = listOf(),
        var radius: Int? = null,
        var matches: List<User> = listOf(),
        var description: String? = null
    ) {

        fun setUsername(username: String) = apply {
            this.username = username
        }

        fun setLocation(location: Location) = apply {
            this.location = location
        }

        fun setBirthday(birthday: String) = apply {
            this.birthday = birthday
        }

        fun setGender(gender: String) = apply {
            this.gender = gender
        }

        fun setSexualOrientations(sexual_orientations: List<String>) = apply {
            this.sexual_orientations = sexual_orientations
        }

        fun setShowMe(showMe: String) = apply {
            this.show_me = showMe
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

        fun build(): User {
            return User(
                username,
                location,
                birthday,
                gender,
                sexual_orientations,
                show_me,
                passions,
                radius,
                matches,
                description
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
                val location = getField<Location>("location")!!
                val birthday = getString("birthday")!!
                val gender = getString("gender")!!
                val sexual_orientations = get("sexual_orientations") as List<String>
                val show_me = getString("show_me")!!
                val passions = get("passions") as List<String>
                val radius = getField<Int>("radius")!!
                val matches = get("matches") as List<User>
                val description = getString("description")!!
                return User(
                    username,
                    location,
                    birthday,
                    gender,
                    sexual_orientations,
                    show_me,
                    passions,
                    radius,
                    matches,
                    description
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                return null
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getUserAge(user: User?): Int? {
            val birthday = user?.birthday?.split('.')
            if (birthday != null) {
                val age = Period.between(
                    LocalDate.of(
                        birthday[2].toInt(),
                        birthday[1].toInt(),
                        birthday[0].toInt()
                    ),
                    LocalDate.now()
                ).years

                return age
            }
            return null
        }
    }

}