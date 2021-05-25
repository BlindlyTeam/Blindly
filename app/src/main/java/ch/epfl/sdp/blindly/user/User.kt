package ch.epfl.sdp.blindly.user

import android.util.Log
import ch.epfl.sdp.blindly.utils.Date
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ktx.getField
import kotlinx.serialization.Serializable

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
const val LIKES = "likes"
const val RECORDING_PATH = "recordingPath"
const val AGE_RANGE = "ageRange"
const val REPORTED = "reported"
const val DELETED = "deleted"

/**
 * A class to represent a User
 */
@Serializable
class User private constructor(
    @Exclude var uid: String?,
    var username: String?,
    var location: List<Double>?,
    var birthday: String?,
    var gender: String?,
    var sexualOrientations: List<String>?,
    var showMe: String?,
    var passions: List<String>?,
    var radius: Int?,
    var matches: List<String>?,
    var likes: List<String>?,
    var recordingPath: String?,
    var ageRange: List<Int>?,
    var deleted: Boolean,
    var reported: List<String>?
) {

    /**
     * A builder used to partially initialize a user during the profile_setup activities
     * Made serializable so that it can be put in a bundle and passed as extra
     *
     * @property uid the uid of the User
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
     * @property recordingPath the path to the recording of the user
     * @property ageRange the ageRange of the User
     */
    @Serializable
    data class Builder(
        var uid: String? = null,
        var username: String? = null,
        var location: List<Double>? = null,
        var birthday: String? = null,
        var gender: String? = null,
        var sexualOrientations: List<String> = listOf(),
        var showMe: String? = null,
        var passions: List<String> = listOf(),
        var radius: Int? = null,
        var matches: List<String> = listOf(),
        var likes: List<String> = listOf(),
        var recordingPath: String? = null,
        var ageRange: List<Int> = listOf()
    ) {
        /**
         * Set the uid in the UserBuilder
         *
         * @param uid the username of the User
         */
        fun setUid(uid: String) = apply {
            this.uid = uid
        }

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
                    "Expected location.size to be " +
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
         * Set the matches in the UserBuilder. Other users are represented by their UID.
         *
         * @param matches the matches of the User
         */
        fun setMatches(matches: List<String>) = apply {
            this.matches = matches
        }

        /**
         * Set the likes in the UserBuilder
         *
         * @param likes the likes of the User. Other users are represented by their UID.
         */
        fun setLikes(likes: List<String>) = apply {
            this.likes = likes
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
         * Build a User from the UserBuilder parameters, by default a User cannot be deleted at
         * creation and the reported list is empty
         *
         * @return a User
         */
        fun build(): User {
            return User(
                uid,
                username,
                location,
                birthday,
                gender,
                sexualOrientations,
                showMe,
                passions,
                radius,
                matches,
                likes,
                recordingPath,
                ageRange,
                false,
                listOf()
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
                val uid = id
                val username = getString(USERNAME)!!
                val location = get(LOCATION) as? List<Double>
                val birthday = getString(BIRTHDAY)!!
                val gender = getString(GENDER)!!
                val sexualOrientations = get(SEXUAL_ORIENTATIONS) as? List<String>
                val showMe = getString(SHOW_ME)!!
                val passions = get(PASSIONS) as? List<String>
                val radius = getField<Int>(RADIUS)!!
                val matches = get(MATCHES) as? List<String>
                val likes = get(LIKES) as? List<String>
                val ageRange = get(AGE_RANGE) as? List<Long>
                val recordingPath = getString(RECORDING_PATH)!!
                val deleted = getField<Boolean>(DELETED)!!
                val reported = get(REPORTED) as? List<String>

                return User(
                    uid,
                    username,
                    location,
                    birthday,
                    gender,
                    sexualOrientations,
                    showMe,
                    passions,
                    radius,
                    matches,
                    likes,
                    recordingPath,
                    listOf(
                        ageRange!![0].toInt(),
                        ageRange[1].toInt()
                    ), //Numbers on Firestore are Long, so we need to cast back to Int
                    deleted,
                    reported
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                return null
            }
        }

        /**
         * Compute the age of the user
         *
         * @param user the user whose age we want to compute
         * @return an Int containing the age of the User
         */
        fun getUserAge(user: User?): Int? {
            val birthday = user?.birthday
            val date = Date.getDate(birthday)
            if (date != null)
                return date.getAge()
            return null
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
                    assertIsString(newValue)
                    user.username = newValue as String
                }
                LOCATION -> {
                    assertIsLocation(newValue)
                    user.location = newValue as List<Double>
                }
                BIRTHDAY -> {
                    assertIsString(newValue)
                    user.birthday = newValue as String
                }
                GENDER -> {
                    assertIsString(newValue)
                    user.gender = newValue as String
                }
                SEXUAL_ORIENTATIONS -> {
                    assertIsListOfString(newValue)
                    user.sexualOrientations = newValue as List<String>
                }
                SHOW_ME -> {
                    assertIsString(newValue)
                    user.showMe = newValue as String
                }
                PASSIONS -> {
                    assertIsListOfString(newValue)
                    user.passions = newValue as List<String>
                }
                RADIUS -> {
                    assertIsInt(newValue)
                    user.radius = newValue as Int
                }
                MATCHES -> {
                    assertIsListOfString(newValue)
                    user.matches = newValue as List<String>
                }
                LIKES -> {
                    assertIsListOfString(newValue)
                    user.likes = newValue as List<String>
                }
                RECORDING_PATH -> {
                    assertIsString(newValue)
                    user.recordingPath = newValue as String
                }
                AGE_RANGE -> {
                    assertIsAgeRange(newValue)
                    user.ageRange = newValue as List<Int>
                }
                REPORTED -> {
                    assertIsListOfString(newValue)
                    user.reported = newValue as List<String>
                }
                DELETED -> {
                    assertIsBoolean(newValue)
                    user.deleted = newValue as Boolean
                }
            }
            return user
        }

        private fun <T> assertIsListOfString(newValue: T) {
            if (newValue !is List<*>)
                throw java.lang.IllegalArgumentException("Expected newValue to be a List<String>")
            if (newValue[0] !is String)
                throw java.lang.IllegalArgumentException("Expected newValue to be a List<String>")
        }

        private fun <T> assertIsAgeRange(newValue: T) {
            if (newValue !is List<*>)
                throw java.lang.IllegalArgumentException("Expected newValue to be a List<Int>")
            if (newValue[0] !is Int)
                throw java.lang.IllegalArgumentException("Expected newValue to be a List<Int>")
            if (newValue.size != SIZE_OF_AGE_RANGE_LIST)
                throw IllegalArgumentException(
                    "Expected ageRange.size to be " +
                            "$SIZE_OF_AGE_RANGE_LIST but got: ${newValue.size} instead"
                )
        }

        private fun <T> assertIsLocation(newValue: T) {
            if (newValue !is List<*>)
                throw java.lang.IllegalArgumentException("Expected newValue to be a List<Double>")
            if (newValue[0] !is Double)
                throw java.lang.IllegalArgumentException("Expected newValue to be a List<Double>")
            if (newValue.size != SIZE_OF_LOCATION_LIST)
                throw IllegalArgumentException(
                    "Expected location.size to be " +
                            "$SIZE_OF_LOCATION_LIST but got: ${newValue.size} instead"
                )
        }

        private fun <T> assertIsString(newValue: T) {
            if (newValue !is String)
                throw java.lang.IllegalArgumentException("Expected newValue to be a String")
        }

        private fun <T> assertIsInt(newValue: T) {
            if (newValue !is Int)
                throw java.lang.IllegalArgumentException("Expected newValue to be an Int")
        }

        private fun <T> assertIsBoolean(newValue: T) {
            if (newValue !is Boolean)
                throw java.lang.IllegalArgumentException("Expected newValue to be a Boolean")
        }

    }

    /* This is for debugging in tests, you're free to modify it if you need to (but don't forget
       to modify the test results in UserUnitTest too) */
    override fun toString(): String {
        return "$username"
    }
}

