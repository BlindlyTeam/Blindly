package ch.epfl.sdp.blindly.dependency_injection

import ch.epfl.sdp.blindly.user.storage.UserCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * User cache module for dependency injections
 */
@Module
@InstallIn(SingletonComponent::class)
object UserCacheModule {

    /**
     * Return a UserCache to be injected
     *
     * @return UserCache
     */
    @Singleton
    @Provides
    fun provideUserCache(): UserCache = UserCache()
}
