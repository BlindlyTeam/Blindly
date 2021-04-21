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

class MatchingAlgorithm {
    @Inject
    private lateinit var userHelper: UserHelper

    @Inject
    private lateinit var userRepository: UserRepository

    private val TAG = "MatchingAlgorithm"

    suspend fun addMatchesFromDatabase(cf: CollectionReference) {
        val currentUser = getCurrentUser()
        val matches: MutableList<User?> = ArrayList<User?>().toMutableList()

        val queryResult = currentUser?.passions?.let {
            cf.whereEqualTo("gender", currentUser.show_me)
                .whereArrayContainsAny("passions", it).get()
                .addOnSuccessListener { users ->
                    for (user in users) {
                        matches += user.toUser()
                    }}
                .addOnFailureListener {
                    Log.w(TAG, "Error getting users : ", it)
                }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun getCurrentUser(): User? {
        return userHelper.getUserId()?.let { userRepository.getUser(it) }
    }
}