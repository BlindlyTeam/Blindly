package ch.epfl.sdp.blindly

import android.os.Handler
import ch.epfl.sdp.blindly.utils.UserHelper
import ch.epfl.sdp.blindly.utils.UserHelperModule
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

        const val USERNAME = "Jane Doe"
        const val BIRTHDAY = "28.03.2003"
        const val LOCATION = "EPFL, Ecublens"
        const val GENRE = "Woman"
        val SEXUAL_ORIENTATIONS = arrayListOf("Straight", "Gay", "Lesbian")
        const val SHOW_ME = "Woman"
        val PASSIONS = arrayListOf("Brunch", "Running", "Wine", "Tea")
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