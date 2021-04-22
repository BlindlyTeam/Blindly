package ch.epfl.sdp.blindly.match

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.User.Companion.toUser
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

private const val ONE_KM_IN_METERS = 1000
private const val TAG = "MatchingAlgorithm"

class MatchingAlgorithm() {
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
     *      TODO: 4) O is in the age range of C, as specified in settings
     *
     * @return a list of users that can be showed in the swiping interface
     */
    suspend fun getPotentialMatchesFromDatabase(): List<User>? {
        val currentUser = getCurrentUser()
        val matches: MutableList<User?> = ArrayList<User?>().toMutableList()

        currentUser?.passions?.let { list ->
            userRepository.getCollectionReference()
                .whereEqualTo("gender", currentUser.show_me)
                .whereArrayContainsAny("passions", list).get()
                .addOnSuccessListener { users ->
                    for (user in users) {
                        matches += user.toUser()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting users : ", exception)
                }
        }
        return currentUser?.let { filterLocationRange(it, clearNullUsers(matches)) }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun getCurrentUser(): User? {
        return userHelper.getUserId()?.let { userRepository.getUser(it) }
    }

    /**
     * Filters a list of users, so that only the ones that are in the location range of the current
     * user remain.
     *
     * @param currentUser the current user
     * @param otherUsers the list of other users
     * @return the filtered list of users
     */
    private fun filterLocationRange(currentUser: User, otherUsers: List<User>): List<User> {
        val filteredList: MutableList<User> = ArrayList<User>().toMutableList()

        for (user in otherUsers) {
            val distance = currentUser.location?.distanceTo(user.location)!!
            val radiusInMeters = currentUser.radius!! * ONE_KM_IN_METERS
            if (distance <= radiusInMeters) {
                filteredList += user
            }
        }

        return filteredList
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