package ch.epfl.sdp.blindly.fake_module

import android.os.Handler
import android.os.Looper
import ch.epfl.sdp.blindly.dependency_injection.FirestoreModule
import ch.epfl.sdp.blindly.fake_module.FakeUserCacheModule.Companion.fakeUser
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
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

        val taskCompletionSource = TaskCompletionSource<DocumentSnapshot>()
        Handler(Looper.getMainLooper()).postDelayed({ taskCompletionSource.setResult(null) }, 1000L)
        val successfulTask = taskCompletionSource.task
        val collection = mock(CollectionReference::class.java)
        val document = mock(DocumentReference::class.java)
        Mockito.`when`(db.collection(USER_COLLECTION)).thenReturn(collection)
        Mockito.`when`(
            db.collection(USER_COLLECTION)
                .document(TEST_UID)
        ).thenReturn(document)
        // TODO db.collection().document().get().await().toUser is a suspend function which is not
        //  the case of the real module.
        //  Need to find a proper way to retrun the fakeUser after await()
        Mockito.`when`(
            db.collection(USER_COLLECTION)
                .document(TEST_UID)
                .get()
        )
            .thenReturn(successfulTask).then { fakeUser }

        return db
    }
}