package ch.epfl.sdp.blindly.match

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.User.Companion.toUser
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

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
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getPotentialMatchesFromDatabase(): List<User>? {
        val userid = userHelper.getUserId()!!
        val currentUser = userRepository.getUser(userid)
        val matches: MutableList<User?> = ArrayList<User?>().toMutableList()
        var query: Query?

        if (currentUser == null) {
            return null
        }

        query = currentUser.passions?.let {
            userRepository.getCollectionReference()
                .whereArrayContainsAny("passions", it)
        }
        if (query != null) {
            if (currentUser.showMe != EVERYONE) {
                query = query.whereEqualTo("gender", currentUser.showMe)
            }

            //Wait on the query to be done before continuing
            try {
                val users = query.get().await()
                for (user in users) {
                    if (user.id != userid) {
                        matches += user.toUser()
                    }
                }
            } catch (exception: Exception) {
                Log.w(TAG, "Error getting users : ", exception)
            }
        }
        val nonNullMatches = matches.filterNotNull()
        val filteredList = userListFilter.filterLocationAndAgeRange(currentUser, nonNullMatches)
        return userListFilter.reversePotentialMatch(currentUser, filteredList)
    }
}