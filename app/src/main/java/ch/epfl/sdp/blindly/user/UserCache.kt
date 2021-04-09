package ch.epfl.sdp.blindly.user

import android.os.Build
import androidx.annotation.RequiresApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UserCacheModule {
    @Provides
    fun provideUserCache(): UserCache = UserCache()
}

/**
 * Local cache used to store user's information
 */
class UserCache {
    private val cache = HashMap<String, User>()

    @RequiresApi(Build.VERSION_CODES.N)
    fun get(uid:String): User? {
        return cache[uid]
    }

    fun put(uid: String, user: User) {
        cache[uid] = user
    }

    fun contains(uid: String): Boolean {
        return cache[uid] != null
    }

}