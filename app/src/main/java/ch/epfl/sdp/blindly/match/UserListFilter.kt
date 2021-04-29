package ch.epfl.sdp.blindly.match

import android.os.Build
import androidx.annotation.RequiresApi
import ch.epfl.sdp.blindly.user.User

private const val ONE_KM_IN_METERS = 1000

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
        val filteredList: MutableList<User> = ArrayList<User>().toMutableList()
        val minAge = currentUser.ageRange[0]
        val maxAge = currentUser.ageRange[1]

        for (user in otherUsers) {
            val otherUserAge = User.getUserAge(user)
            if (otherUserAge != null) {
                if (otherUserAge in minAge..maxAge && isLocatedInUserRadius(currentUser, user)) {
                    filteredList += user
                }
            }
        }

        return filteredList
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
        val filteredList: MutableList<User> = ArrayList<User>().toMutableList()
        val currentUserGender = currentUser.gender
        val currentUserAge = User.getUserAge(currentUser)

        for (user in otherUsers) {
            if (currentUserAge in user.ageRange[0]..user.ageRange[1] &&
                isLocatedInUserRadius(user, currentUser)
            ) {
                if (user.showMe == EVERYONE) {
                    filteredList += user
                } else if (user.showMe == currentUserGender) {
                    filteredList += user
                }
            }
        }

        return filteredList
    }

    // Doesn't work right now, since the locations don't work
    private fun isLocatedInUserRadius(user: User, otherUser: User): Boolean {
        val distance = user.location?.distanceTo(otherUser.location)!!
        val radiusInMeters = user.radius!! * ONE_KM_IN_METERS
        return distance <= radiusInMeters
    }
}