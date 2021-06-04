package ch.epfl.sdp.blindly.main_screen.match.algorithm

import android.util.Log
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper

private const val TAG = "MatchingAlgorithm"
const val EVERYONE = "Everyone"

/**
 * Handles the matching algorithm that we use in our app.
 */
class MatchingAlgorithm(
    private var userHelper: UserHelper,
    private var userRepository: UserRepository
) {
    private val userListFilter = UserListFilter()

    /**
     * Gets potential matches from the user's informations. Here are the specifications:
     * (C = current user, O = other user)
     *      1) O's gender corresponds to C's show me
     *      2) At least one passion of C is in common with O
     *      3) O is in the location radius of C
     *      4) O is in the age range of C, as specified in settings
     *
     * Then, everything is reverse checked from the point of view of the other user by calling
     * [UserListFilter.reversePotentialMatch].
     *
     * @return a list of users that can be showed in the swiping interface
     */
    suspend fun getPotentialMatchesFromDatabase(): List<User>? {
        val userid = userHelper.getUserId()!!
        val currentUser = userRepository.getUser(userid) ?: return null

        val query = currentUser.passions?.let {
            UserRepository.Query(passions = it)
        }
        if (query != null) {
            if (currentUser.showMe != EVERYONE) {
                query.gender = currentUser.showMe
            }

            //Wait on the query to be done before continuing
            try {
                val matches = userRepository.query(query).filter { user ->
                    user.uid != userid
                    && !currentUser.likes!!.contains(user.uid)
                    && !currentUser.dislikes!!.contains(user.uid)
                }
                val nonDeletedMatches = matches.filter { user -> !user.deleted }
                val filteredList =
                    userListFilter.filterLocationAndAgeRange(currentUser, nonDeletedMatches)
                return userListFilter.reversePotentialMatch(currentUser, filteredList)
            } catch (exception: Exception) {
                Log.w(TAG, "Error getting users : ", exception)
            }
        }
        return null
    }
}