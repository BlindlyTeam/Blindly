package ch.epfl.sdp.blindly.fake_module

import android.os.Handler
import android.os.Looper
import ch.epfl.sdp.blindly.di.FirestoreModule
import ch.epfl.sdp.blindly.di.UserRepositoryModule
import ch.epfl.sdp.blindly.fake_module.FakeUserCacheModule.Companion.fakeUser
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.User.Companion.toUser
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.tasks.await
import org.mockito.Mockito
import org.mockito.Mockito.mock
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FirestoreModule::class]
)
open class FakeFirestoreModule {
    companion object {
        private const val USER_COLLECTION: String = "usersMeta"
    }

    @Singleton
    @Provides
    open fun provideFirebaseFirestore(): FirebaseFirestore {
        val db = mock(FirebaseFirestore::class.java)

        val taskCompletionSource = TaskCompletionSource<DocumentSnapshot>();
        Handler(Looper.getMainLooper()).postDelayed({ taskCompletionSource.setResult(null) }, 1000L);
        val successfulTask = taskCompletionSource.task;
        val collection = mock(CollectionReference::class.java)
        val document = mock(DocumentReference::class.java)
        Mockito.`when`(db.collection(USER_COLLECTION)).thenReturn(collection)
        Mockito.`when`(db.collection(USER_COLLECTION)
                .document(TEST_UID)).thenReturn(document)
        Mockito.`when`(db.collection(USER_COLLECTION)
                .document(TEST_UID)
                .get())
                .thenReturn(successfulTask).then {fakeUser}
        return db
    }
}