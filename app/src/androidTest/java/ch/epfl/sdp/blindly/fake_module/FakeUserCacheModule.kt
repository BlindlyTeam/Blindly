package ch.epfl.sdp.blindly.fake_module

import ch.epfl.sdp.blindly.di.UserCacheModule
import ch.epfl.sdp.blindly.user.UserCache
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@TestInstallIn(
        components = [SingletonComponent::class],
        replaces = [UserCacheModule::class]
)

open class FakeUserCacheModule {
    @Singleton
    @Provides
    open fun provideUserCache(): UserCache {
        return Mockito.mock(UserCache::class.java)
    }
}