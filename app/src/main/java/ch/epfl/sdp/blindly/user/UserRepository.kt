package ch.epfl.sdp.blindly.user

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ch.epfl.sdp.blindly.user.User.Companion.toUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@Module
@InstallIn(SingletonComponent::class)
class UserRepositoryModule {
    @Inject
    lateinit var db: FirebaseFirestore
    @Inject
    lateinit var userCache: UserCache
    @Provides
    fun provideUserRepository(): UserRepository = UserRepository(db, userCache)
}


class UserRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val userCache: UserCache) {

    companion object {
        private const val TAG = "FirebaseProfileService"
        private const val USER_COLLECTION: String = "usersMeta"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getUser(uid: String): User? {
        val cached: User? = userCache.get(uid)
        if (cached != null) {
            return cached
        }
        return refreshUser(uid)
    }

    private suspend fun refreshUser(uid: String): User? {
        return try {
            val freshUser = db.collection(USER_COLLECTION)
                .document(uid).get().await().toUser()
            if (freshUser != null) {
                userCache.put(uid, freshUser)
            }
            return freshUser
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            null
        }
    }

    /**
     * field: the field of the value to change inside the database
     * newValue: the new value to set for the user
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