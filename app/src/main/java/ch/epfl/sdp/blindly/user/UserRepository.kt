package ch.epfl.sdp.blindly.user

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ch.epfl.sdp.blindly.user.User.Companion.toUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This is the main access point to firestore
 */

class UserRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val userCache: UserCache) {

    companion object {
        private const val TAG = "UserRepository"
        private const val USER_COLLECTION: String = "usersMeta"
    }

    /**
     * Given a uid, if the user is cached locally return this user, otherwise
     * look for the user in firestore and update the cache
     *
     * @param uid: The uid of the user to retrieve
     * @return the user with the corresponding uid or null if they doesn't exist
     */
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getUser(uid: String): User? {
        val cached: User? = userCache.get(uid)
        if (cached != null) {
            Log.d(TAG, "Found user with uid: $uid in cache")
            return cached
        }
        return refreshUser(uid)
    }

    /**
     * Look for the user with the corresponding uid in firestore and store it in the local cache
     *
     * @param uid: The uid of the user to retrieve in firestore
     * @return the user with the corresponding uid or null if he/she/it doesn't exist
     */
    suspend fun refreshUser(uid: String): User? {
        return try {
            val freshUser = db.collection(USER_COLLECTION)
                .document(uid).get().await().toUser()
            if (freshUser != null) {
                Log.d(TAG, "Put User \"$uid\" in local cache")
                userCache.put(uid, freshUser)
            }
            Log.d(TAG, "Retrieve User \"$uid\" in firestore")
            return freshUser
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            null
        }
    }

    /**
     * Update a given field of the user's information (and call refreshUser to update or set the
     * user in the local cache)
     *
     * @param uid: the uid of the user to update
     * @param field: the field of the value to change inside the database
     * @param newValue: the new value to set for the user
     */
    /*@RequiresApi(Build.VERSION_CODES.N)
    suspend fun <T> updateProfile(uid: String, field: String, newValue: T) {
        if(newValue !is String || newValue !is ArrayList<*>)
            throw IllegalArgumentException("Expected String or ArrayList<String>")
        db.collection(USER_COLLECTION)
            .document(uid)
            .update(field, newValue)
        refreshUser(uid)
    }*/
}