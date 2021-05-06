package ch.epfl.sdp.blindly.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Firestore module for dependy injection
 */
@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    /**
     * Return a Firestore object to be injected
     *
     * @return Firestore
     */
    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore
}