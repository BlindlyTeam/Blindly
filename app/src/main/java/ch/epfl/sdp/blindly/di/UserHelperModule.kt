package ch.epfl.sdp.blindly.di

import ch.epfl.sdp.blindly.user.UserHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Module to be installed in Activities
@Module
@InstallIn(SingletonComponent::class)
object UserHelperModule {
    @Provides
    fun provideUserHelper(): UserHelper = UserHelper()
}