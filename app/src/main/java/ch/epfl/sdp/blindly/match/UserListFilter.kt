package ch.epfl.sdp.blindly.match

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import ch.epfl.sdp.blindly.user.User
import java.lang.IllegalArgumentException

private const val ONE_KM_IN_METERS = 1000

/**
 * Class used to perform filtering on a list of User. It is used only in the [MatchingAlgorithm],
 * but needed to be tested.
 */
class UserListFilter {

    /**
     * Filters a list of users, so that only the ones that are in the location and age range of
     * the current user remain.
     *
     * @param currentUser the current user
     * @param otherUsers the list of other users
     * @return the filtered list of users
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun filterLocationAndAgeRange(currentUser: User, otherUsers: List<User>): List<User> {
        val minAge = currentUser.ageRange?.get(0)
        val maxAge = currentUser.ageRange?.get(1)

        return otherUsers.filter { user ->
            User.getUserAge(user) in minAge!!..maxAge!! && isLocatedInUserRadius(currentUser, user)
        }
    }

    /**
     * Does the same checks than [MatchingAlgorithm.getPotentialMatchesFromDatabase], but from the
     * point of view of the other user. The returned list keeps only the users that can be matched
     * with the current user.
     *
     * @param currentUser
     * @param otherUsers
     * @return the filtered list of user
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun reversePotentialMatch(currentUser: User, otherUsers: List<User>): List<User> {
        val currentUserGender = currentUser.gender
        val currentUserAge = User.getUserAge(currentUser)

        return otherUsers.filter { user ->
            if (user.showMe == EVERYONE) {
                currentUserAge in user.ageRange?.get(0)!!..user.ageRange!![1] &&
                        isLocatedInUserRadius(user, currentUser)
            } else {
                currentUserAge in user.ageRange?.get(0)!!..user.ageRange!![1] &&
                        isLocatedInUserRadius(user, currentUser) &&
                        user.showMe == currentUserGender
            }
        }
    }

    private fun isLocatedInUserRadius(user: User, otherUser: User): Boolean {
        if (user.location == null || otherUser.location == null) {
            throw IllegalArgumentException("Cannot perform distance between null locations")
        }
        val location = createLocation(user.location!!)
        val otherLocation = createLocation(otherUser.location!!)
        val distance = location.distanceTo(otherLocation)
        val radiusInMeters = (user.radius!! * ONE_KM_IN_METERS).toFloat()
        return distance <= radiusInMeters
    }

    private fun createLocation(locationTable: List<Double>): Location {
        val location = Location("")
        location.latitude = locationTable[0]
        location.longitude = locationTable[1]
        return location
    }
}