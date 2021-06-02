package ch.epfl.sdp.blindly.database

import androidx.lifecycle.LifecycleOwner
import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatch
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
     * Get the location of the user, wrap it as a BlindlyLatLng
     * and return it to use with WeatherActivity
     *
     * @param uid UID of the current user
     * @return a BlindlyLatLng location for weather activity
     */
    suspend fun getLocation(uid: String): BlindlyLatLng

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
     * Removes a match from cuurent user, by deleting it from likes and matches
     * and then adding to dislikes (to prevent reappear)
     *
     * @param userId current user's ID
     * @param matchId matched user's ID
     */
    suspend fun removeMatchFromCurrentUser(userId: String, matchId: String)

    /**
     * Removes the current user from removed user's matches
     * It's kept in likes of remote user so that they wouldn't reappear
     * in their cards
     *
     * @param removingUserId
     * @param removedUserId
     */
    suspend fun removeCurrentUserFromRemovedMatch(removingUserId : String, removedUserId : String)

    /**
     * Remove a user from either the Matches or Liked list from all user that contains them
     *
     * @param field either MATCHES or LIKES
     * @param uid the uid of the user to remove from all lists
     */
    suspend fun removeFieldFromUser(field: String, uid: String)


    /**
     * Deletes a user
     *
     * @param uid the uid of th euser to delete
     */
    suspend fun deleteUser(uid: String)

    /**
     * Asynchronously get the users
     *
     * @return The list of the matching users for a query
     */
    suspend fun query(query: Query): List<User>

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
    class Query(var passions: List<String>? = null, var gender: String? = null)
}