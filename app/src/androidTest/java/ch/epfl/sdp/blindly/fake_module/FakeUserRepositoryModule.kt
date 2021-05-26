package ch.epfl.sdp.blindly.fake_module

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.dependency_injection.UserRepositoryModule
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatch
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.User.Companion.updateUser
import ch.epfl.sdp.blindly.user.storage.UserCache
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
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import javax.inject.Singleton
import kotlin.reflect.KSuspendFunction1

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UserRepositoryModule::class]
)
open class FakeUserRepositoryModule {

    companion object {
        private const val USER_COLLECTION: String = "usersMeta"
        private const val uid = TEST_UID
        private const val username = "Jane Doe"
        private const val usernameUpdated = "Jack"
        private val location =
            AndroidLocationService.createLocationTableEPFL() // Ecublens, Switzerland
        private const val MULHOUSE_LAT = 47.749
        private const val MULHOUSE_LON = 7.335
        private val locationUpdated = listOf(MULHOUSE_LAT, MULHOUSE_LON) // Mulhouse, France
        private const val birthday = "01.01.01"
        private const val gender = "Woman"
        private const val genderUpdated = "Man"
        private val sexualOrientations = listOf("Asexual")
        private val sexualOrientationsUpdated = listOf("Asexual", "Bisexual")
        private const val showMe = "Everyone"
        private const val showMeUpdated = "Women"
        private val passions = listOf("Coffee", "Tea")
        private val passionsUpdated = listOf("Coffee", "Tea", "Movies", "Brunch")
        private const val radius = 150
        private const val radiusUpdated = 50
        private val matches: List<String> = listOf("a1", "b2")
        private val likes: List<String> = listOf("c3", "d4")
        private const val recordingPath =
            "Recordings/OKj1UxZao3hIVtma95gWZlner9p1-PresentationAudio.amr"
        private val ageRange = listOf(30, 50)
        private val ageRangeUpdated = listOf(40, 50)
        val fakeUser = User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes)
            .setRecordingPath(recordingPath)
            .setAgeRange(ageRange)
            .build()
        val fakeUserUpdated = User.Builder()
            .setUid(uid)
            .setUsername(usernameUpdated)
            .setLocation(locationUpdated)
            .setBirthday(birthday)
            .setGender(genderUpdated)
            .setSexualOrientations(sexualOrientationsUpdated)
            .setShowMe(showMeUpdated)
            .setPassions(passionsUpdated)
            .setRadius(radiusUpdated)
            .setMatches(matches)
            .setLikes(likes)
            .setRecordingPath(recordingPath)
            .setAgeRange(ageRangeUpdated)
            .build()
    }

    @Singleton
    @Provides
    open fun provideUserCache(): UserCache {
        val userCache = mock(UserCache::class.java)
        Mockito.`when`(userCache.get(TEST_UID)).thenReturn(fakeUser)
        return userCache
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
        //TODO db.collection().document().get().await().toUser is a suspend function which is not the case of the real module
        // Need to find a proper way to retrun the fakeUser after await()
        Mockito.`when`(
            db.collection(USER_COLLECTION)
                .document(TEST_UID)
                .get()
        )
            .thenReturn(successfulTask).then { fakeUser }

        return db
    }

    @Singleton
    @Provides
    open fun provideUserRepository(): UserRepository {
        val userRepository = mock(UserRepository::class.java)

        userRepository.stub {
            onBlocking { userRepository.getUser(TEST_UID) }.doReturn(fakeUser)
            onBlocking { userRepository.refreshUser(TEST_UID) }.doReturn(fakeUser)
        }
        //return UserRepositoryModule.provideUserRepository()
        return (object: UserRepository {
            val db = HashMap<String, User>()
            override suspend fun getUser(uid: String): User? {
                return db.getOrDefault(uid, fakeUser)
            }

            override suspend fun refreshUser(uid: String): User? {
                return db.getOrDefault(uid, fakeUser)
            }

            override suspend fun <T> updateProfile(uid: String, field: String, newValue: T) {
                val updatedUser = updateUser(getUser(uid)!!.copy(), field, newValue)
                db[uid] = updatedUser
            }

            override suspend fun query(query: UserRepository.Query): List<User> {
                return db.values.toList()
            }

            override suspend fun getMyMatches(
                viewLifecycleOwner: LifecycleOwner,
                userId: String,
                setupAdapter: KSuspendFunction1<MutableList<MyMatch>, Unit>
            ) {
                setupAdapter(mutableListOf(MyMatch("aaa", "222", true)))
            }

        })
    }
}