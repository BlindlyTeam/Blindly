package ch.epfl.sdp.blindly.fake_module

import ch.epfl.sdp.blindly.di.UserRepositoryModule
import ch.epfl.sdp.blindly.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@TestInstallIn(
        components = [SingletonComponent::class],
        replaces = [UserRepositoryModule::class]
)

open class FakeUserRepositoryModule {
    @Singleton
    @Provides
    open fun provideUserRepository(): UserRepository {
        return Mockito.mock(UserRepository::class.java)
    }
}