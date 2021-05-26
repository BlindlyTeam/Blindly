package ch.epfl.sdp.blindly.fake_module

import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.dependency_injection.UserRepositoryModule
import ch.epfl.sdp.blindly.fake_module.FakeUserCacheModule.Companion.fakeUser
import ch.epfl.sdp.blindly.fake_module.FakeUserCacheModule.Companion.fakeUser2
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID2
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
        Mockito.`when`(userRepository.getUser(TEST_UID2)).thenReturn(fakeUser2)
        Mockito.`when`(userRepository.refreshUser(TEST_UID2)).thenReturn(fakeUser2)
        return userRepository
    }
}