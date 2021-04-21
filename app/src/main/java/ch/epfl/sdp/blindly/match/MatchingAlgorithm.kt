package ch.epfl.sdp.blindly.match

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.User.Companion.toUser
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.GeoPoint
import javax.inject.Inject

class MatchingAlgorithm(private var collectionReference: CollectionReference) {
    @Inject
    private lateinit var userHelper: UserHelper

    @Inject
    private lateinit var userRepository: UserRepository

    companion object {
        private const val TAG = "MatchingAlgorithm"
    }

    /**
     * Gets potential matches from the user's informations. Here are the specifications:
     * (C = current user, O = other user)
     *      1) O's gender corresponds to C's show me
     *      2) At least one passion of C is in common with O
     *      3) O is in the location range of C
     *      TODO: 4) O is in the age range of C, as specified in settings
     *
     * @return a list of users that can be showed in the swiping interface
     */
    suspend fun getPotentialMatchesFromDatabase(): List<User>? {
        val currentUser = getCurrentUser()
        val matches: MutableList<User?> = ArrayList<User?>().toMutableList()

        currentUser?.passions?.let {
            collectionReference.whereEqualTo("gender", currentUser.show_me)
                .whereArrayContainsAny("passions", it).get()
                .addOnSuccessListener { users ->
                    for (user in users) {
                        matches += user.toUser()
                    }}
                .addOnFailureListener {
                    Log.w(TAG, "Error getting users : ", it)
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
        val result: MutableList<User> = ArrayList<User>().toMutableList()

        for (user in otherUsers) {
            val distance = currentUser.location?.distanceTo(user.location)!!
            val radiusInMeters = currentUser.radius!! * 1000
            if (distance <= radiusInMeters) {
                result += user
            }
        }

        return result
    }

    private fun clearNullUsers(userList: List<User?>): List<User> {
        val result: MutableList<User> = ArrayList<User>().toMutableList()
        for (user in userList) {
            if (user != null) {
                result += user
            }
        }
        return result
    }
}