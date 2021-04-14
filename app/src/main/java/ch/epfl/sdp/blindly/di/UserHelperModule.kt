package ch.epfl.sdp.blindly.di

import ch.epfl.sdp.blindly.user.UserHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

// Module to be installed in Activities
@Module
@InstallIn(SingletonComponent::class)
object UserHelperModule {

    @Singleton
    @Provides
    fun provideUserHelper(): UserHelper = UserHelper()

    @Singleton
    @Provides
    @Named("userHelperWithUid")
    fun provideUserHelperWithUid(): UserHelper = UserHelper()
}