package ch.epfl.sdp.blindly.fake_module

import android.os.Handler
import ch.epfl.sdp.blindly.di.UserHelperModule
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@TestInstallIn(
        components = [SingletonComponent::class],
        replaces = [UserHelperModule::class]
)
// Replace the UserHelper with a mock for testing.
open class FakeUserHelperModule {
    companion object {
        const val PRIMARY_EMAIL = "test@example.com";
        const val SECOND_EMAIL = "test2@example.com";
    }

    @Singleton
    @Provides
    open fun provideUserHelper(): UserHelper {
        val user = Mockito.mock(UserHelper::class.java)
        Mockito.`when`(user.getEmail()).thenReturn(PRIMARY_EMAIL)
        val taskCompletionSource = TaskCompletionSource<Void>();

        Handler().postDelayed({ taskCompletionSource.setResult(null) }, 1000L);

        val successfulTask = taskCompletionSource.task;

        Mockito.`when`(user.setEmail(SECOND_EMAIL)).thenReturn(successfulTask)
        return user
    }

    @Singleton
    @Provides
    open fun provideFirebaseFirestore(): FirebaseFirestore {
        return Mockito.mock(FirebaseFirestore::class.java)
    }
}