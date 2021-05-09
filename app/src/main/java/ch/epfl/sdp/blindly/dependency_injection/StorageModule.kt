package ch.epfl.sdp.blindly.dependency_injection

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Storage module for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    /**
     * Return a storage object to be injected
     *
     * @return Storage
     */
    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage
}