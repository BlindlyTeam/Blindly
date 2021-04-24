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
 * A class to represent a User
 *
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
     * Made serializable so that it can be put in a bundle and passed as extra
     *
     * @property username the username of the User
     * @property location the location of the User
     * @property birthday the birthday of the User
     * @property gender the gender of the User
     * @property sexualOrientations a List<String> containing the
     *     sexual orientations of the User
     * @property showMe the show_me of the User
     * @property passions an List<String> containing the
     *     passions of the User
     * @property radius the radius in which the User want the matching algorithm to look in
     * @property matches a List<User> containing the Users the User has a match with
     * @property description the description of the User
     * @property ageRange the ageRange of the User
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

        /**
         * Set the username in the UserBuilder
         *
         * @param username the username of the User
         */
        fun setUsername(username: String) = apply {
            this.username = username
        }

        /**
         * Set the location in the UserBuilder
         *
         * @param location the location of the User
         */
        fun setLocation(location: String) = apply {
            this.location = location
        }

        /**
         * Set the birthday in the UserBuidler
         *
         * @param birthday the birthday of the User
         */
        fun setBirthday(birthday: String) = apply {
            this.birthday = birthday
        }

        /**
         * Set the gender in the UserBuilder
         *
         * @param gender the gender of the User
         */
        fun setGender(gender: String) = apply {
            this.gender = gender
        }

        /**
         * Set the sexual orientations in the UserBuilder
         *
         * @param sexual_orientations the sexual orientations of the User
         */
        fun setSexualOrientations(sexual_orientations: List<String>) = apply {
            this.sexualOrientations = sexual_orientations
        }

        /**
         * Set the show me in the UserBuidler
         *
         * @param showMe the show me of the User
         */
        fun setShowMe(showMe: String) = apply {
            this.showMe = showMe
        }

        /**
         * Set the passions in the UserBuilder
         *
         * @param passions the passions of the User
         */
        fun setPassions(passions: List<String>) = apply {
            this.passions = passions
        }

        /**
         * Set the radius in the UserBuilder
         *
         * @param radius the radius of the User
         */
        fun setRadius(radius: Int) = apply {
            this.radius = radius
        }

        /**
         * Set the matches in the UserBuilder
         *
         * @param matches the matches of the User
         */
        fun setMatches(matches: List<User>) = apply {
            this.matches = matches
        }

        /**
         * Set the description in the UserBuilder
         *
         * @param description the description of the User
         */
        fun setDescription(description: String) = apply {
            this.description = description
        }

        /**
         * Set the age range in the UserBuilder
         *
         * @param ageRange a list containing the age range :
         *     ageRange[0] = minAge,
         *     ageRange[1] = maxAge
         */
        fun setAgeRange(ageRange: List<Int>) = apply {
            this.ageRange = ageRange
        }

        /**
         * Build a User from the UserBuilder parameters
         *
         * @return a User
         */
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
         *
         * @return a User
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

        /**
         * Compute the age of the user
         *
         * @param user: the user whose age we want to compute
         * @return a String containing the age of the User
         */
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
