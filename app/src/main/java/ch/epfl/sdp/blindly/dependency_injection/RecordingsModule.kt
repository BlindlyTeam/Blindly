package ch.epfl.sdp.blindly.dependency_injection

import ch.epfl.sdp.blindly.audio.FirebaseRecordingsImpl
import ch.epfl.sdp.blindly.audio.FirebaseRecordings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Firestore module for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object RecordingsModule {

    /**
     * Return a Firestore object to be injected
     *
     * @return Firestore
     */
    @Singleton
    @Provides
    fun provideRecordingsModule(): FirebaseRecordings = FirebaseRecordingsImpl(Firebase.storage)
}