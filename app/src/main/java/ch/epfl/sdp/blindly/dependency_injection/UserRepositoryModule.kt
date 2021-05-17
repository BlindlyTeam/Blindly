package ch.epfl.sdp.blindly.dependency_injection

import ch.epfl.sdp.blindly.user.storage.UserCache
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.database.UserRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * UserRepository module for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    /**
     * Return a UserRepository to be injected
     *
     * @param db the firebase firestore database
     * @param userCache
     * @return a UserRepository with these database and cache
     */
    @Singleton
    @Provides
    fun provideUserRepository(db: FirebaseFirestore, userCache: UserCache):
            UserRepository = UserRepositoryImpl(db, userCache)
}