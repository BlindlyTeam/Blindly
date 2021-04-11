package ch.epfl.sdp.blindly

import android.os.Handler
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserHelperModule
import com.google.android.gms.tasks.TaskCompletionSource
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
}