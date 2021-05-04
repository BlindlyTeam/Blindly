package ch.epfl.sdp.blindly.user

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.Period

private const val SIZE_OF_LOCATION_LIST = 2
private const val SIZE_OF_AGE_RANGE_LIST = 2

const val USERNAME = "username"
const val LOCATION = "location"
const val BIRTHDAY = "birthday"
const val GENDER = "gender"
const val SEXUAL_ORIENTATIONS = "sexualOrientations"
const val SHOW_ME = "showMe"
const val PASSIONS = "passions"
const val RADIUS = "radius"
const val MATCHES = "matches"
const val DESCRIPTION = "description"
const val RECORDING_PATH = "recordingPath"
const val AGE_RANGE = "ageRange"

/**
 * A class to represent a User
 *
 */
@Serializable
class User private constructor(
    var username: String?,
    var location: List<Double>?,
    var birthday: String?,
    var gender: String?,
    var sexualOrientations: List<String>?,
    var showMe: String?,
    var passions: List<String>?,
    var radius: Int?,
    var matches: List<String>?,
    var description: String?,
    var recordingPath: String?,
    var ageRange: List<Int>?
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
     * @property passions a List<String> containing the
     *     passions of the User
     * @property radius the radius in which the User want the matching algorithm to look in
     * @property matches a List<User> containing the Users the User has a match with
     * @property description the description of the User
     * @property recordingPath the path to the recording of the user
     * @property ageRange the ageRange of the User
     */
    @Serializable
    data class Builder(
        var username: String? = null,
        var location: List<Double>? = null,
        var birthday: String? = null,
        var gender: String? = null,
        var sexualOrientations: List<String> = listOf(),
        var showMe: String? = null,
        var passions: List<String> = listOf(),
        var radius: Int? = null,
        var matches: List<String> = listOf(),
        var description: String? = null,
        var recordingPath: String? = null,
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
        fun setLocation(location: List<Double>) = apply {
            if (location.size == SIZE_OF_LOCATION_LIST)
                this.location = location
            else
                throw IllegalArgumentException(
                    "Expected ageRange.size to be " +
                            "$SIZE_OF_LOCATION_LIST but got: ${location.size} instead"
                )
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
         * @param sexualOrientations the sexual orientations of the User
         */
        fun setSexualOrientations(sexualOrientations: List<String>) = apply {
            this.sexualOrientations = sexualOrientations
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
        fun setMatches(matches: List<String>) = apply {
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
         * Set the recording path in the UserBuilder
         *
         * @param recordingPath the path to the recording
         */
        fun setRecordingPath(recordingPath: String) = apply {
            this.recordingPath = recordingPath
        }

        /**
         * Set the age range in the UserBuilder
         *
         * @param ageRange a list containing the age range :
         *     ageRange[0] = minAge,
         *     ageRange[1] = maxAge
         */
        fun setAgeRange(ageRange: List<Int>) = apply {
            if (ageRange.size == SIZE_OF_AGE_RANGE_LIST)
                this.ageRange = ageRange
            else
                throw IllegalArgumentException(
                    "Expected ageRange.size to be " +
                            "$SIZE_OF_AGE_RANGE_LIST but got: ${ageRange.size} instead"
                )
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
                recordingPath,
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
                val location = get("location") as? List<Double>
                val birthday = getString("birthday")!!
                val gender = getString("gender")!!
                val sexualOrientations = get("sexualOrientations") as? List<String>
                val showMe = getString("showMe")!!
                val passions = get("passions") as? List<String>
                val radius = getField<Int>("radius")!!
                val matches = get("matches") as? List<String>
                val description = getString("description")!!
                val ageRange = get("ageRange") as? List<Int>
                val recordingPath = getString("recordingPath")!!

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
                    recordingPath,
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
         * @return an Int containing the age of the User
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

        /**
         * Update the field of a User
         *
         * @param T either a String, an Int or a List<*>
         * @param user the user to update
         * @param field the field to update
         * @param newValue the new value
         * @return the updated user
         */
        fun <T> updateUser(user: User, field: String, newValue: T): User {
            when (field) {
                USERNAME -> {
                    if (newValue !is String)
                        throw java.lang.IllegalArgumentException("Expected newValue to be a String")
                    user.username = newValue
                }
                LOCATION -> {
                    if (newValue !is List<*>)
                        throw java.lang.IllegalArgumentException("Expected newValue to be a List<Double>")
                    if (newValue.size == SIZE_OF_LOCATION_LIST)
                        user.location = newValue as List<Double>
                    else
                        throw IllegalArgumentException(
                            "Expected ageRange.size to be " +
                                    "$SIZE_OF_LOCATION_LIST but got: ${newValue.size} instead"
                        )
                }
                BIRTHDAY -> {
                    if (newValue !is String)
                        throw java.lang.IllegalArgumentException("Expected newValue to be a String")
                    user.birthday = newValue
                }
                GENDER -> {
                    if (newValue !is String)
                        throw java.lang.IllegalArgumentException("Expected newValue to be a String")
                    user.gender = newValue
                }
                SEXUAL_ORIENTATIONS -> {
                    if (newValue !is List<*>)
                        throw java.lang.IllegalArgumentException("Expected newValue to be a List<String>")
                    user.sexualOrientations = newValue as List<String>
                }
                SHOW_ME -> {
                    if (newValue !is String)
                        throw java.lang.IllegalArgumentException("Expected newValue to be a String")
                    user.showMe = newValue
                }
                PASSIONS -> {
                    if (newValue !is List<*>)
                        throw java.lang.IllegalArgumentException("Expected newValue to be a List<String>")
                    user.passions = newValue as List<String>
                }
                RADIUS -> {
                    if (newValue !is Int)
                        throw java.lang.IllegalArgumentException("Expected newValue to be an Int")
                    user.radius = newValue
                }
                MATCHES -> {
                    if (newValue !is List<*>)
                        throw java.lang.IllegalArgumentException("Expected newValue to be a List<String>")
                    user.matches = newValue as List<String>
                }
                DESCRIPTION -> {
                    if (newValue !is String)
                        throw java.lang.IllegalArgumentException("Expected newValue to be a String")
                    user.description = newValue
                }
                RECORDING_PATH -> {
                    if (newValue !is String)
                        throw java.lang.IllegalArgumentException("Expected newValue to be a String")
                    user.recordingPath = newValue
                }
                AGE_RANGE -> {
                    if (newValue !is List<*>)
                        throw java.lang.IllegalArgumentException("Expected newValue to be a List<Int>")
                    if (newValue.size == SIZE_OF_AGE_RANGE_LIST)
                        user.ageRange = newValue as List<Int>
                    else
                        throw IllegalArgumentException(
                            "Expected ageRange.size to be " +
                                    "$SIZE_OF_AGE_RANGE_LIST but got: ${newValue.size} instead"
                        )
                }
            }
            return user
        }
    }

    /* This is for debugging in tests, you're free to modify it if you need to (but don't forget
       to modify the test results in UserUnitTest too) */
    override fun toString(): String {
        return "$username"
    }

}

