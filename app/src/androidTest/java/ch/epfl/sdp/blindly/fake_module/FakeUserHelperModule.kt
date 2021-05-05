package ch.epfl.sdp.blindly.fake_module

import android.content.Intent
import android.os.Handler
import android.os.Looper
import ch.epfl.sdp.blindly.di.UserHelperModule
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.android.gms.tasks.TaskCompletionSource
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import org.mockito.Mockito.mock
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UserHelperModule::class]
)
open class FakeUserHelperModule {
    companion object {
        const val PRIMARY_EMAIL = "test@example.com"
        const val SECOND_EMAIL = "test2@example.com"
        const val TEST_UID = "DBrGTHNkj9Z3VaKIeQCJrL3FANg2"
    }

    @Singleton
    @Provides
    open fun provideUserHelper(): UserHelper {
        val user = mock(UserHelper::class.java)
        Mockito.`when`(user.getEmail()).thenReturn(PRIMARY_EMAIL)
        val taskCompletionSource = TaskCompletionSource<Void>()

        Handler(Looper.getMainLooper()).postDelayed({ taskCompletionSource.setResult(null) }, 1000L)

        val successfulTask = taskCompletionSource.task

        Mockito.`when`(user.setEmail(SECOND_EMAIL)).thenReturn(successfulTask)

        Mockito.`when`(user.getUserId()).thenReturn(TEST_UID)

        //TODO this fakeIntent may be wrong to fake
        val fakeIntent = mock(Intent::class.java)
        Mockito.`when`(user.getSignInIntent()).thenReturn(fakeIntent)

        Mockito.`when`(user.isLoggedIn()).thenReturn(true)
        return user
    }
}