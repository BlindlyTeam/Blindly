package ch.epfl.sdp.blindly.match

import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.User.Companion.toUser
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import javax.inject.Inject

private const val ONE_KM_IN_METERS = 1000
private const val TAG = "MatchingAlgorithm"
private const val EVERYONE = "Everyone"

class MatchingAlgorithm {
    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var userRepository: UserRepository

    /**
     * Gets potential matches from the user's informations. Here are the specifications:
     * (C = current user, O = other user)
     *      1) O's gender corresponds to C's show me
     *      2) At least one passion of C is in common with O
     *      3) O is in the location radius of C
     *      4) O is in the age range of C, as specified in settings
     * Then, everything is reverse checked from the point of view of the other user by calling
     * [reverseCheck]
     *
     * @return a list of users that can be showed in the swiping interface
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getPotentialMatchesFromDatabase(): List<User>? {
        val currentUser = getCurrentUser()
        val matches: MutableList<User?> = ArrayList<User?>().toMutableList()
        var query: Query?

        query = currentUser?.passions?.let {
            userRepository.getCollectionReference().whereArrayContainsAny("passions", it)
        }

        if (currentUser!!.showMe != EVERYONE) {
            query = query?.whereEqualTo("gender", currentUser.showMe)
        }

        query?.get()?.addOnSuccessListener { users ->
            for (user in users) {
                matches += user.toUser()
            }
        }?.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting users : ", exception)
        }

        val filteredList = filterLocationAndAgeRange(currentUser, clearNullUsers(matches))
        return reverseCheck(currentUser, filteredList)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun getCurrentUser(): User? {
        return userHelper.getUserId()?.let { userRepository.getUser(it) }
    }

    /**
     * Filters a list of users, so that only the ones that are in the location and age range of
     * the current user remain.
     *
     * @param currentUser the current user
     * @param otherUsers the list of other users
     * @return the filtered list of users
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterLocationAndAgeRange(currentUser: User, otherUsers: List<User>): List<User> {
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
     * Does the same checks than [getPotentialMatchesFromDatabase], but from the point of view of
     * the other user. The returned list keeps only the users that can be matched with the current
     * user.
     *
     * @param currentUser
     * @param otherUsers
     * @return the filtered list of user
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun reverseCheck(currentUser: User, otherUsers: List<User>): List<User> {
        val filteredList: MutableList<User> = ArrayList<User>().toMutableList()
        val currentUserGender = currentUser.gender
        val currentUserAge = User.getUserAge(currentUser)

        for (user in otherUsers) {
            if (currentUserAge in user.ageRange[0]..user.ageRange[1] &&
                isLocatedInUserRadius(user, currentUser)) {
                if (user.showMe == EVERYONE) {
                    filteredList += user
                } else if (user.showMe == currentUserGender) {
                        filteredList += user
                }
            }
        }

        return filteredList
    }

    private fun isLocatedInUserRadius(user: User, otherUser: User): Boolean {
        val distance = user.location?.distanceTo(otherUser.location)!!
        val radiusInMeters = user.radius!! * ONE_KM_IN_METERS
        return distance <= radiusInMeters
    }

    private fun clearNullUsers(userList: List<User?>): List<User> {
        val nonNullList: MutableList<User> = ArrayList<User>().toMutableList()
        for (user in userList) {
            if (user != null) {
                nonNullList += user
            }
        }
        return nonNullList
    }
}