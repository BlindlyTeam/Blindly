package ch.epfl.sdp.blindly.fake_module

import android.content.Intent
import android.os.Handler
import android.os.Looper
import ch.epfl.sdp.blindly.audio.Recordings
import ch.epfl.sdp.blindly.dependency_injection.RecordingsModule
import ch.epfl.sdp.blindly.dependency_injection.UserHelperModule
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.android.gms.tasks.TaskCompletionSource
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import org.mockito.Mockito.mock
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

    }
}