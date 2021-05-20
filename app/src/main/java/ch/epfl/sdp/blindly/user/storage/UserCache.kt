package ch.epfl.sdp.blindly.user.storage

import ch.epfl.sdp.blindly.user.User

/**
 * The local cache used to store the user's information
 */
class UserCache {
    private val cache = HashMap<String, User>()

    /**
     * Get the user with corresponding uid
     *
     * @param uid the uid of the user to retrieve
     * @return The User if it was cached, null otherwise
     */
    fun get(uid: String): User? {
        return cache[uid]
    }

    /**
     * Put the given the User in the cache
     *
     * @param uid the key
     * @param user the value
     */
    fun put(uid: String, user: User) {
        cache[uid] = user
    }

    /**
     * Check if there's an entry uid in the cache
     *
     * @param uid the uid to check
     * @return true if the cache contains uid, false otherwise
     */
    fun contains(uid: String): Boolean {
        return cache[uid] != null
    }
}