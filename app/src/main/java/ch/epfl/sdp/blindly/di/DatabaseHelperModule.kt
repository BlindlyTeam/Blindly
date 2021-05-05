package ch.epfl.sdp.blindly.di

import ch.epfl.sdp.blindly.helpers.DatatbaseHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * UserHelper Module fore dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseHelperModule {

    /**
     * Return a UserHelper to be injected
     *
     * @return UserHelper
     */
    @Singleton
    @Provides
    fun provideDatabaseHelper(): DatatbaseHelper = DatatbaseHelper()
}