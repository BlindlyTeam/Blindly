package ch.epfl.sdp.blindly.fake_module

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.dependency_injection.UserRepositoryModule
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID2
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatch
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatch
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
import ch.epfl.sdp.blindly.user.LIKES
import ch.epfl.sdp.blindly.user.MATCHES
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
        private const val uid2 = TEST_UID2
        private const val username = "Jane Doe"
        private const val username2 = "Jack"
        private val location =
            AndroidLocationService.createLocationTableEPFL() // Ecublens, Switzerland
        private const val MULHOUSE_LAT = 47.749
        private const val MULHOUSE_LON = 7.335
        private val location2 = listOf(MULHOUSE_LAT, MULHOUSE_LON) // Mulhouse, France
        private const val birthday = "01.01.01"
        private const val gender = "Woman"
        private const val gender2 = "Man"
        private val sexualOrientations = listOf("Asexual")
        private val sexualOrientations2 = listOf("Asexual", "Bisexual")
        private const val showMe = "Everyone"
        private const val showMe2 = "Women"
        private val passions = listOf("Coffee", "Tea")
        private val passions2 = listOf("Coffee", "Tea", "Movies", "Brunch")
        private const val radius = 150
        private const val radius2 = 50
        private val matches: List<String> = listOf(TEST_UID2)
        private val matches2: List<String> = listOf(TEST_UID)
        private val likes: List<String> = listOf(TEST_UID2)
        private val likes2: List<String> = listOf(TEST_UID)
        private const val recordingPath =
            "Recordings/OKj1UxZao3hIVtma95gWZlner9p1-PresentationAudio.amr"
        private val ageRange = listOf(30, 50)
        private val ageRange2 = listOf(40, 50)

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
        val fakeUser2 = User.Builder()
            .setUid(uid2)
            .setUsername(username2)
            .setLocation(location2)
            .setBirthday(birthday)
            .setGender(gender2)
            .setSexualOrientations(sexualOrientations2)
            .setShowMe(showMe2)
            .setPassions(passions2)
            .setRadius(radius2)
            .setMatches(matches2)
            .setLikes(likes2)
            .setRecordingPath(recordingPath)
            .setAgeRange(ageRange2)
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
            onBlocking { userRepository.getUser(TEST_UID2) }.doReturn(fakeUser2)
            onBlocking { userRepository.refreshUser(TEST_UID2) }.doReturn(fakeUser2)
        }
        
        return (Mockito.spy(object: UserRepository {
            val db = HashMap<String, User>()
            
            override suspend fun getUser(uid: String): User? {
                return db.getOrDefault(uid, fakeUser)
            }

            override suspend fun removeMatchFromAUser(
                field: String,
                userId: String,
                matchId: String
            ) {
                var updatedList: ArrayList<String>? = arrayListOf()
                val user = getUser(userId)
                if (user != null) {
                    when (field) {
                        LIKES ->
                            updatedList = user.likes as ArrayList<String>?
                        MATCHES ->
                            updatedList = user.matches as ArrayList<String>?
                    }
                    updatedList?.remove(matchId)
                    if (user != null) {
                        user.uid?.let { updateProfile(it, field, updatedList) }
                    }
                }
            }

            override suspend fun getLocation(uid: String): BlindlyLatLng {
                val user = db.getOrDefault(uid, fakeUser)
                if (user != null) {
                    return BlindlyLatLng(user.location?.get(0), user.location?.get(1))
                }
                return BlindlyLatLng(LAUSANNE_LATLNG)
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
                setupAdapter(mutableListOf(MyMatch(fakeUser2.username!!, fakeUser2.uid!!, false)))
            }
        }))
    }
}