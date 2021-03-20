package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.utils.UserHelper
import ch.epfl.sdp.blindly.utils.UserHelperModule
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
    @Singleton
    @Provides
    open fun provideUserHelper(): UserHelper {
        val user = Mockito.mock(UserHelper::class.java)
        Mockito.`when`(user.getEmail()).thenReturn("test@example.com")
        return user
    }
}