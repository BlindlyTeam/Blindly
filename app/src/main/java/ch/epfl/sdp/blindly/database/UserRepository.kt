package ch.epfl.sdp.blindly.database

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import ch.epfl.sdp.blindly.database.localDB.AppDatabase
import ch.epfl.sdp.blindly.database.localDB.UserDAO
import ch.epfl.sdp.blindly.database.localDB.UserEntity
import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatch
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
import ch.epfl.sdp.blindly.user.LIKES
import ch.epfl.sdp.blindly.user.MATCHES
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.User.Companion.toUser
import ch.epfl.sdp.blindly.user.storage.UserCache
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.reflect.KSuspendFunction1

/**
 * The main access to Firebase firestore
 *
 * @property db the instance of FirebaseFirestore
 * @property userCache the local cache
 */
class UserRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val userCache: UserCache,
    localDB: AppDatabase,
) {

    private val userDAO: UserDAO = localDB.UserDAO()

    companion object {
        private const val TAG = "UserRepository"
        private const val USER_COLLECTION: String = "usersMeta"
    }

    /**
     * Given a uid, if the user is cached locally return this user, otherwise
     * look for the user in firestore and update the cache
     *
     * @param uid the uid of the user to retrieve
     * @return the user with the corresponding uid or null if they doesn't exist
     */
    suspend fun getUser(uid: String): User? {
        val cached: User? = userCache.get(uid)
        if (cached != null) {
            Log.d(TAG, "Found user with uid: $uid in cache")
            return cached
        }
        val localUser: User?
        withContext(Dispatchers.IO) {
            localUser = userDAO.getUser(uid)
        }
        if(localUser != null) {
            Log.d(TAG, "Found user with uid: $uid in local DB")
            return localUser
        }
        return refreshUser(uid)
    }

    /**
     * Removes another liked or matched user from current user.
     *
     * @param field field to remove a User (either from LIKES or MATCHES)
     * @param userId current user's ID
     * @param matchId matched user's ID
     */
    suspend fun removeMatchFromAUser(field: String, userId: String, matchId:String) {
        var updatedList: ArrayList<String>? = arrayListOf()
        val user = getUser(userId)
        if (user != null) {
            when (field) {
                LIKES ->
                    updatedList = user.likes as ArrayList<String>?
                MATCHES ->
                    updatedList = user.matches as ArrayList<String>?
            }
            updatedList?.remove(matchId)
            if (user != null) {
                user.uid?.let { updateProfile(it, field, updatedList) }
            }
        }
    }

    /**
     * Get the location of the user, wrap it as a BlindlyLatLng
     * and return it to use with WeatherActivity
     *
     * @param uid UID of the current user
     * @return a BlindlyLatLng location for weather activity
     */
    suspend fun getLocation(uid: String): BlindlyLatLng {
        val user = getUser(uid)
        if (user != null) {
            return BlindlyLatLng(user.location?.get(0), user.location?.get(1))
        }
        return BlindlyLatLng(LAUSANNE_LATLNG)
    }

    /**
     * Look for the user with the corresponding uid in firestore and store it in the local cache
     *
     * @param uid the uid of the user to retrieve in firestore
     * @return the user with the corresponding uid or null if he/she/it doesn't exist
     */
    suspend fun refreshUser(uid: String): User? {
        return try {
            val freshUser = db.collection(USER_COLLECTION)
                .document(uid).get().await().toUser()
            if (freshUser != null) {
                Log.d(TAG, "Put User \"$uid\" in local cache and local DB")
                userCache.put(uid, freshUser)
                withContext(Dispatchers.IO) {
                    userDAO.insertUser(UserEntity(uid, freshUser))
                }
            }
            Log.d(TAG, "Retrieve User \"$uid\" in firestore")
            return freshUser
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            null
        }
    }

    private suspend fun <T> updateLocalCacheAndDB(uid: String, field: String, newValue: T) {
        val user = userCache.get(uid)
        if (user != null) {
            Log.d(TAG, "Updated user in local cache and local DB")
            val updatedUser = User.updateUser(user, field, newValue)
            userCache.put(uid, updatedUser)
            withContext(Dispatchers.IO) {
                userDAO.updateUser(UserEntity(uid, updatedUser))
            }
        } else {
            refreshUser(uid)
        }
    }

    /**
     * Update a given field of the user's information (and call refreshUser to update or set the
     * user in the local cache)
     *
     * @param uid the uid of the user to update
     * @param field the field of the value to change inside the database
     * @param newValue the new value to set for the user
     */
    suspend fun <T> updateProfile(uid: String, field: String, newValue: T) {
        if (newValue !is String && newValue !is List<*> && newValue !is Int)
            throw IllegalArgumentException("Expected String, List<String> or Int")

        db.collection(USER_COLLECTION)
            .document(uid)
            .update(field, newValue)
        Log.d(TAG, "Updated user")
        //Put updated value into the local cache
        updateLocalCacheAndDB(uid, field, newValue)
    }

    /**
     * Get the collection reference of the database of users.
     *
     * @return the reference of the database
     */
    fun getCollectionReference(): CollectionReference {
        return db.collection(USER_COLLECTION)
    }

    suspend fun getMyMatches(
        viewLifecycleOwner: LifecycleOwner,
        userId: String,
        setupAdapter: KSuspendFunction1<ArrayList<MyMatch>, Unit>
    ) {
        var myMatchesUids: List<String>
        var myMatches: ArrayList<MyMatch>?
        val docRef = getCollectionReference().document(userId)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
                myMatchesUids = snapshot["matches"] as List<String>
                viewLifecycleOwner.lifecycleScope.launch {
                    myMatches = arrayListOf()
                    for (userId in myMatchesUids) {
                        myMatches!!.add(
                            MyMatch(
                                getUser(userId)?.username!!,
                                userId,
                                false
                            )
                        )
                    }
                    setupAdapter(myMatches!!)

                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }
}
