package ch.epfl.sdp.blindly.fake_module

import ch.epfl.sdp.blindly.audio.Recordings
import ch.epfl.sdp.blindly.dependency_injection.RecordingsModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import java.io.File
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RecordingsModule::class]
)
open class FakeRecordingsModule {

    @Singleton
    @Provides
    open fun provideRecordingsModule(): Recordings = object : Recordings {
        override fun putFile(
            recordingPath: String,
            file: File,
            callback: Recordings.RecordingOperationCallback
        ) {
            callback.onSuccess()
        }

        override fun getFile(
            recordingPath: String,
            file: File,
            callback: Recordings.RecordingOperationCallback
        ) {
            callback.onSuccess()
        }

        override fun deleteFile(
            recordingPath: String,
            callback: Recordings.RecordingOperationCallback
        ) {
            callback.onSuccess()
        }
    }
}