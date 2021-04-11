package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.user.UserCache
import ch.epfl.sdp.blindly.user.UserCacheModule
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
        replaces = [UserCacheModule::class]
)

open class FakeUserCache {
    @Singleton
    @Provides
    open fun provideUserCache(): UserCache {
        return Mockito.mock(UserCache::class.java)
    }
}