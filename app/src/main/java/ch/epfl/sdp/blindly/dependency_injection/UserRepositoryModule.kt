package ch.epfl.sdp.blindly.dependency_injection

import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.database.UserRepositoryImpl
import ch.epfl.sdp.blindly.user.storage.UserCache
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * UserRepository module for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    val firestore get() = Firebase.firestore
    val userCache = UserCache()
    /**
     * Return a Firestore object to be injected
     *
     * @return Firestore
     */
    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = firestore

    /**
     * Return a UserCache to be injected
     *
     * @return UserCache
     */
    @Singleton
    @Provides
    fun provideUserCache(): UserCache = UserCache()

    /**
     * Return a UserRepository to be injected
     *
     * @return a UserRepository with these database and cache
     */
    @Singleton
    @Provides
    fun provideUserRepository():
            UserRepository = UserRepositoryImpl(firestore, userCache)
}