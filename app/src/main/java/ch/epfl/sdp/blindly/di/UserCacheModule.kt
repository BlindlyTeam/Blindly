package ch.epfl.sdp.blindly.di

import ch.epfl.sdp.blindly.user.UserCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UserCacheModule {

    @Singleton
    @Provides
    fun provideUserCache(): UserCache = UserCache()
}
