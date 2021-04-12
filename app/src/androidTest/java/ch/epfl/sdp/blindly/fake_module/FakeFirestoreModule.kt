package ch.epfl.sdp.blindly.fake_module

import ch.epfl.sdp.blindly.di.UserRepositoryModule
import ch.epfl.sdp.blindly.user.UserRepository
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
    replaces = [UserRepositoryModule::class]
)

open class FakeFirestoreModule {
    @Singleton
    @Provides
    open fun provideFirebaseFirestore(): FirebaseFirestore {
        return Mockito.mock(FirebaseFirestore::class.java)
    }
}