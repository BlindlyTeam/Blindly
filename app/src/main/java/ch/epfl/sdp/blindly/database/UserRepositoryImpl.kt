package ch.epfl.sdp.blindly.database

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import ch.epfl.sdp.blindly.database.localDB.AppDatabase
import ch.epfl.sdp.blindly.database.localDB.UserEntity
import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatch
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
import ch.epfl.sdp.blindly.user.*
import ch.epfl.sdp.blindly.user.User.Companion.toUser
import ch.epfl.sdp.blindly.user.storage.UserCache
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.reflect.KSuspendFunction1

/**
 * The main access to Firebase firestore
 *
 * @property db the instance of FirebaseFirestore
 * @property userCache the local cache
 */
class UserRepositoryImpl constructor(
    private val db: FirebaseFirestore,
    private val userCache: UserCache,
    localDB: AppDatabase,
) : UserRepository {

    private val userDAO = localDB.UserDAO()

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
    override suspend fun getUser(uid: String): User? {
        val cached: User? = userCache.get(uid)
        if (cached != null) {
            Log.d(TAG, "Found user with uid: $uid in cache")
            return cached
        }
        /*
        val local: User?
        withContext(Dispatchers.IO) {
            local = userDAO.getUser(uid)
        }
        if (local != null) {
            Log.d(TAG, "Found user with uid: $uid in localDB")
            return local
        }
         */
        return refreshUser(uid)
    }

    /**
     * Get the location of the user, wrap it as a BlindlyLatLng
     * and return it to use with WeatherActivity
     *
     * @param uid UID of the current user
     * @return a BlindlyLatLng location for weather activity
     */
    override suspend fun getLocation(uid: String): BlindlyLatLng {
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
    override suspend fun refreshUser(uid: String): User? {
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
            Log.d(TAG, "Updated user in local cache and localDB")
            userCache.put(uid, User.updateUser(user, field, newValue))
            withContext(Dispatchers.IO) {
                userDAO.updateUser(UserEntity(uid, user))
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
    override suspend fun <T> updateProfile(uid: String, field: String, newValue: T) {
        Log.d(TAG, "Updating field: $field")
        if (newValue !is String && newValue !is List<*> && newValue !is Int && newValue !is Boolean)
            throw IllegalArgumentException("Expected String, List<String> or Int")

        db.collection(USER_COLLECTION)
            .document(uid)
            .update(field, newValue)
        Log.d(TAG, "Updated user")
        //Put updated value into the local cache
        updateLocalCacheAndDB(uid, field, newValue)
    }

    /**
     * Deletes a user
     *
     * @param uid the uid of th euser to delete
     */
    override suspend fun deleteUser(uid: String) {
        removeFieldFromUser(LIKES, uid)
        updateProfile(uid, DELETED, true)
        userCache.remove(uid)
        val user = userDAO.getUser(uid)
        userDAO.deleteUser(UserEntity(uid, user!!))
    }

    /**
     * Remove a user from either the Matches or Liked list from all user that contains them
     *
     * @param field either MATCHES or LIKES
     * @param uid the uid of the user to remove from all lists
     */
    override suspend fun removeFieldFromUser(field: String, uid: String) {
        if (field != MATCHES && field != LIKES)
            throw java.lang.IllegalArgumentException("Expected filed to be MATCHES or LIKES")
        var updatedList: ArrayList<String>? = null
        val snapshot = db.collection(USER_COLLECTION).whereArrayContains(field, uid).get().await()
        val users = snapshot.map { s -> s.toUser() }
        users.forEach { user ->
            if (user != null) {
                when (field) {
                    LIKES ->
                        updatedList = user.likes as ArrayList<String>?
                    MATCHES ->
                        updatedList = user.matches as ArrayList<String>?
                }
                updatedList?.remove(uid)
                user.uid?.let { updateProfile(it, field, updatedList) }
            }
        }
    }


    /**
     * Removes another liked or matched user from current user.
     *
     * @param field field to remove a User (either from LIKES or MATCHES)
     * @param userId current user's ID
     * @param matchId matched user's ID
     */
    override suspend fun removeMatchFromAUser(field: String, userId: String, matchId: String) {
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
            user.uid?.let { updateProfile(it, field, updatedList) }
        }
    }

    /**
     * Get the collection reference of the database of users.
     *
     * @return the reference of the database
     */
    private fun getCollectionReference(): CollectionReference {
        return db.collection(USER_COLLECTION)
    }

    /**
     * Asynchronously get the users
     *
     * @return The list of the matching users for this query
     */
    override suspend fun query(query: UserRepository.Query): List<User> {
        val internalQuery = getCollectionReference()
        return query(query, internalQuery)
    }

    private suspend fun query(
        query: UserRepository.Query,
        internalQuery: com.google.firebase.firestore.Query
    ): List<User> {
        query.passions?.let { internalQuery.whereArrayContainsAny(PASSIONS, it) }
        query.gender?.let { internalQuery.whereEqualTo(GENDER, it) }

        return internalQuery.get().await()
            .mapNotNull { queryDocumentSnapshot -> queryDocumentSnapshot.toUser() }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getMyMatches(
        viewLifecycleOwner: LifecycleOwner,
        userId: String,
        setupAdapter: KSuspendFunction1<MutableList<MyMatch>, Unit>
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
                myMatchesUids = snapshot[MATCHES] as List<String>
                viewLifecycleOwner.lifecycleScope.launch {
                    myMatches = arrayListOf()
                    for (uid in myMatchesUids) {
                        val user = getUser(uid)
                        myMatches!!.add(
                            MyMatch(
                                user?.username!!,
                                uid,
                                false,
                                user.deleted
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
