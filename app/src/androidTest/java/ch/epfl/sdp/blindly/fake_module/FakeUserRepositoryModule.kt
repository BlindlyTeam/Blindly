package ch.epfl.sdp.blindly.fake_module

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.database.UserRepositoryImpl
import ch.epfl.sdp.blindly.dependency_injection.UserRepositoryModule
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatch
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
import ch.epfl.sdp.blindly.user.DELETED
import ch.epfl.sdp.blindly.user.LIKES
import ch.epfl.sdp.blindly.user.MATCHES
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.User.Companion.toUser
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
import kotlinx.coroutines.tasks.await
import org.mockito.Mockito
import org.mockito.Mockito.mock
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
        private const val MULHOUSE_LAT = 47.749
        private const val MULHOUSE_LON = 7.335

        val fakeUser = User.Builder()
            .setUid("a1")
            .setUsername("Jane Doe")
            .setLocation(AndroidLocationService.createLocationTableEPFL())
            .setBirthday("01.01.2001")
            .setGender("Woman")
            .setSexualOrientations(listOf("Asexual"))
            .setShowMe("Everyone")
            .setPassions(listOf("Coffee", "Tea"))
            .setRadius(150)
            .setMatches(listOf("a2", "a3"))
            .setLikes(listOf("a2", "a3","c3", "d4"))
            .setRecordingPath("Recordings/a1-PresentationAudio.amr")
            .setAgeRange(listOf(18, 50))
            .build()
        val fakeUserUpdated = User.Builder()
            .setUid("a1")
            .setUsername("Jack")
            .setLocation(listOf(MULHOUSE_LAT, MULHOUSE_LON))
            .setBirthday("01.01.2001")
            .setGender("Man")
            .setSexualOrientations(listOf("Asexual", "Bisexual"))
            .setShowMe("Women")
            .setPassions(listOf("Coffee", "Tea", "Movies", "Brunch"))
            .setRadius(50)
            .setMatches(listOf("a2", "a3"))
            .setLikes(listOf("a2", "a3","c3", "d4"))
            .setRecordingPath("Recordings/a1-PresentationAudio.amr")
            .setAgeRange(listOf(40, 50))
            .build()

        val fakeUser2 = User.Builder()
            .setUid("a2")
            .setUsername("Jean Paul")
            .setLocation(AndroidLocationService.createLocationTableEPFL())
            .setBirthday("02.02.2002")
            .setGender("Man")
            .setSexualOrientations(listOf("Straight", "Bisexual"))
            .setShowMe("Everyone")
            .setPassions(listOf("Coffee", "Tea", "Movies", "Brunch"))
            .setRadius(50)
            .setMatches(listOf("a1", "a3", "b5"))
            .setLikes(listOf("a1", "a3", "b5", "d4"))
            .setRecordingPath("Recordings/a2-PresentationAudio.amr")
            .setAgeRange(listOf(18, 50))
            .build()

        val fakeUser3 = User.Builder()
            .setUid("a3")
            .setUsername("Jeanette")
            .setLocation(AndroidLocationService.createLocationTableEPFL())
            .setBirthday("03.03.2003")
            .setGender("Album")
            .setSexualOrientations(listOf("Straight", "Bisexual"))
            .setShowMe("Everyone")
            .setPassions(listOf("Coffee", "Tea", "Movies", "Brunch"))
            .setRadius(50)
            .setMatches(listOf("a1", "a2", "b5"))
            .setLikes(listOf("a1", "a2", "b5", "d4"))
            .setRecordingPath("Recordings/a3-PresentationAudio.amr")
            .setAgeRange(listOf(18, 50))
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

            override suspend fun removeFieldFromUser(field: String, uid: String) {
                if (field != MATCHES && field != LIKES)
                    throw java.lang.IllegalArgumentException("Expected filed to be MATCHES or LIKES")
                var updatedList: ArrayList<String>? = null
                var users = db.values.toList()
                users = if(field == MATCHES)
                    users.filter { user -> user.matches!!.contains(uid) }
                else
                    users.filter { user -> user.likes!!.contains(uid) }
                users.forEach { user ->
                    if (user != null) {
                        when (field) {
                            LIKES ->
                                updatedList = user.likes as ArrayList<String>?
                            MATCHES ->
                                updatedList = user.matches as ArrayList<String>?
                        }
                        updatedList?.remove(uid)
                        user.uid?.let { updateProfile(it, field, updatedList) }
                    }
                }
            }

            override suspend fun deleteUser(uid: String) {
                removeFieldFromUser(LIKES, uid)
                updateProfile(uid, DELETED, true)
                //userCache.remove(uid)
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
                setupAdapter(mutableListOf(MyMatch(fakeUser.username!!, fakeUser.uid!!, true, false)))
            }

        }))
    }
}