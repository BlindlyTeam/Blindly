package ch.epfl.sdp.blindly.dependency_injection

import ch.epfl.sdp.blindly.helpers.DatatbaseHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DatabaseHelper Module for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseHelperModule {

    /**
     * Return a DatabaseHelper to be injected
     *
     * @return DatabaseHelper
     */
    @Singleton
    @Provides
    fun provideDatabaseHelper(): DatatbaseHelper = DatatbaseHelper()
}