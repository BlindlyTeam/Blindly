package ch.epfl.sdp.blindly.fake_module

import ch.epfl.sdp.blindly.di.UserRepositoryModule
import ch.epfl.sdp.blindly.fake_module.FakeUserCacheModule.Companion.fakeUser
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID
import ch.epfl.sdp.blindly.user.UserRepository
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
        replaces = [UserRepositoryModule::class]
)
open class FakeUserRepositoryModule {

    @Singleton
    @Provides
    open suspend fun provideUserRepository(): UserRepository {
        val userRepository = mock(UserRepository::class.java)
        Mockito.`when`(userRepository.getUser(TEST_UID)).thenReturn(fakeUser)
        Mockito.`when`(userRepository.refreshUser(TEST_UID)).thenReturn(fakeUser)
        return userRepository
    }
}