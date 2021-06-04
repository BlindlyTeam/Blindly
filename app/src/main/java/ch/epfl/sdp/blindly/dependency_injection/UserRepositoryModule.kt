package ch.epfl.sdp.blindly.dependency_injection

import android.content.Context
import androidx.room.Room
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.database.localDB.AppDatabase
import ch.epfl.sdp.blindly.database.UserRepositoryImpl
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.storage.UserCache
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

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
     * Return a AppDatabase ot be injected
     *
     * @param appContext
     * @return AppDatabase
     */
    @Singleton
    @Provides
    fun provideLocalDB(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "LocalDatabase").build()
    }

    /**
     * Return a UserRepository to be injected
     *
     * @param appContext
     * @return UserRepository
     */
    @Singleton
    @Provides
    fun provideUserRepository(@ApplicationContext appContext: Context):
            UserRepository = UserRepositoryImpl(firestore, userCache, provideLocalDB(appContext))

    /**
     * Return a UserHelper to be injected
     *
     * @return UserHelper
     */
    @Singleton
    @Provides
    fun provideUserHelper(): UserHelper = UserHelper()
}