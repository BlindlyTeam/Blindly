package ch.epfl.sdp.blindly.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import ch.epfl.sdp.blindly.main_screen.match.my_matches.MyMatch
import ch.epfl.sdp.blindly.user.User
import kotlin.reflect.KSuspendFunction1

interface UserRepository {
    /**
     * Given a uid, if the user is cached locally return this user, otherwise
     * look for the user in firestore and update the cache
     *
     * @param uid the uid of the user to retrieve
     * @return the user with the corresponding uid or null if they doesn't exist
     */
    suspend fun getUser(uid: String): User?

    /**
     * Look for the user with the corresponding uid in firestore and store it in the local cache
     *
     * @param uid the uid of the user to retrieve in firestore
     * @return the user with the corresponding uid or null if he/she/it doesn't exist
     */
    suspend fun refreshUser(uid: String): User?

    /**
     * Update a given field of the user's information (and call refreshUser to update or set the
     * user in the local cache)
     *
     * @param uid the uid of the user to update
     * @param field the field of the value to change inside the database
     * @param newValue the new value to set for the user
     */

    suspend fun <T> updateProfile(uid: String, field: String, newValue: T)

    /**
     * Asynchronously get the users
     *
     * @return The list of the matching users for a query
     */
    suspend fun query(query: Query): List<User>

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getMyMatches(
        viewLifecycleOwner: LifecycleOwner,
        userId: String,
        setupAdapter: KSuspendFunction1<MutableList<MyMatch>, Unit>
    )

    /**
     * Query for the user repositiry
     *
     * @property passions the passions the user must have
     * @property gender the gender the user must have
     */
    class Query(var passions: List<String>? = null, var gender: String? = null )
}