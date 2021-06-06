package ch.epfl.sdp.blindly.fake_module

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.dependency_injection.UserRepositoryModule
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatch
import ch.epfl.sdp.blindly.user.*
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
import kotlinx.coroutines.launch
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
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
        const val PRIMARY_EMAIL = "test@example.com"
        const val SECOND_EMAIL = "test2@example.com"
        private const val USER_COLLECTION: String = "usersMeta"
        private const val MULHOUSE_LAT = 47.749
        private const val MULHOUSE_LON = 7.335

        const val TEST_UID = "DBrGTHNkj9Z3VaKIeQCJrL3FANg2"
        const val TEST_UID2 = "fdJofwEJWflhwjVREs324cdEWals"
        const val TEST_UID3 = "adnDEO28fWLCEJWo234fwCWLjlw"
        const val TEST_UID4 = "gaCDWOIQFJf2439dsafnqkq93"

        val fakeUser = User.Builder()
            .setUid(TEST_UID)
            .setUsername("Jane Doe")
            .setLocation(AndroidLocationService.createLocationTableEPFL())
            .setBirthday("01.01.2001")
            .setGender("Woman")
            .setSexualOrientations(listOf("Asexual"))
            .setShowMe("Everyone")
            .setPassions(listOf("Coffee", "Tea"))
            .setRadius(150)
            .setMatches(listOf(TEST_UID2, TEST_UID3, TEST_UID4))
            .setLikes(listOf(TEST_UID2, TEST_UID3, TEST_UID4))
            .setRecordingPath("Recordings/a1-PresentationAudio.amr")
            .setAgeRange(listOf(18, 50))
            .build()

        val fakeUserUpdated = User.Builder()
            .setUid(TEST_UID)
            .setUsername("Jack")
            .setLocation(listOf(MULHOUSE_LAT, MULHOUSE_LON))
            .setBirthday("01.01.2001")
            .setGender("Man")
            .setSexualOrientations(listOf("Asexual", "Bisexual"))
            .setShowMe("Women")
            .setPassions(listOf("Coffee", "Tea", "Movies", "Brunch"))
            .setRadius(50)
            .setMatches(listOf("a2", "a3"))
            .setLikes(listOf("a2", "a3", "c3", "d4"))
            .setRecordingPath("Recordings/a1-PresentationAudio.amr")
            .setAgeRange(listOf(40, 50))
            .build()

        val fakeUser2 = User.Builder()
            .setUid(TEST_UID2)
            .setUsername("Jean Paul")
            .setLocation(AndroidLocationService.createLocationTableEPFL())
            .setBirthday("02.02.2002")
            .setGender("Man")
            .setSexualOrientations(listOf("Straight", "Bisexual"))
            .setShowMe("Everyone")
            .setPassions(listOf("Coffee", "Tea", "Movies", "Brunch"))
            .setRadius(50)
            .setMatches(listOf(TEST_UID))
            .setLikes(listOf(TEST_UID))
            .setRecordingPath("Recordings/a2-PresentationAudio.amr")
            .setAgeRange(listOf(18, 50))
            .build()

        val fakeUser3 = User.Builder()
            .setUid(TEST_UID3)
            .setUsername("Jeanette")
            .setLocation(AndroidLocationService.createLocationTableEPFL())
            .setBirthday("03.03.2003")
            .setGender("Album")
            .setSexualOrientations(listOf("Straight", "Bisexual"))
            .setShowMe("Everyone")
            .setPassions(listOf("Coffee", "Tea", "Movies", "Brunch"))
            .setRadius(50)
            .setMatches(listOf(TEST_UID))
            .setLikes(listOf(TEST_UID))
            .setRecordingPath("Recordings/a3-PresentationAudio.amr")
            .setAgeRange(listOf(18, 50))
            .build()

        val fakeUser4 = User.Builder()
            .setUid(TEST_UID4)
            .setUsername("Eve")
            .setLocation(AndroidLocationService.createLocationTableEPFL())
            .setBirthday("04.04.1994")
            .setGender("Woman")
            .setSexualOrientations(listOf("Straight", "Bisexual"))
            .setShowMe("Everyone")
            .setPassions(listOf("Coffee", "Tea", "Movies", "Brunch"))
            .setRadius(50)
            .setMatches(listOf(TEST_UID))
            .setLikes(listOf(TEST_UID))
            .setRecordingPath("Recordings/a4-PresentationAudio.amr")
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
        return (Mockito.spy(object : UserRepository {
            val db = hashMapOf(
                TEST_UID to fakeUser,
                TEST_UID2 to fakeUser2,
                TEST_UID3 to fakeUser3,
                TEST_UID4 to fakeUser4,
            )

            override suspend fun getUser(uid: String): User {
                return db.getOrDefault(uid, fakeUser)
            }

            override suspend fun removeMatchFromCurrentUser(
                userId: String,
                matchId: String
            ) {
                val user = getUser(userId)
                val updatedLikesList = user.likes?.toMutableList()
                val updatedDislikesList = user.dislikes?.toMutableList()
                val updatedMatchesList = user.matches?.toMutableList()

                updatedLikesList?.remove(matchId)
                updatedDislikesList?.add(matchId)
                updatedMatchesList?.remove(matchId)

                user.uid?.let { updateProfile(it, LIKES, updatedLikesList) }
                user.uid?.let { updateProfile(it, DISLIKES, updatedDislikesList) }
                user.uid?.let { updateProfile(it, MATCHES, updatedMatchesList) }
            }

            override suspend fun removeFieldFromUser(field: String, uid: String) {
                if (field != MATCHES && field != LIKES)
                    throw java.lang.IllegalArgumentException("Expected filed to be MATCHES or LIKES")
                var updatedList: MutableList<String>? = null
                var users = db.values.toList()
                users = if (field == MATCHES)
                    users.filter { user -> user.matches!!.contains(uid) }
                else
                    users.filter { user -> user.likes!!.contains(uid) }
                users.forEach { user ->
                    when (field) {
                        LIKES ->
                            updatedList = user.likes?.toMutableList()
                        MATCHES ->
                            updatedList = user.matches?.toMutableList()
                    }
                    updatedList?.remove(uid)
                    user.uid?.let { updateProfile(it, field, updatedList) }
                }
            }

            override suspend fun deleteUser(uid: String) {
                removeFieldFromUser(LIKES, uid)
                updateProfile(uid, DELETED, true)
                //userCache.remove(uid)
            }

            override suspend fun getLocation(uid: String): BlindlyLatLng {
                val user = db.getOrDefault(uid, fakeUser)
                return BlindlyLatLng(user.location?.get(0), user.location?.get(1))
            }

            override suspend fun refreshUser(uid: String): User {
                return db.getOrDefault(uid, fakeUser)
            }

            override suspend fun <T> updateProfile(uid: String, field: String, newValue: T) {
                val updatedUser = updateUser(getUser(uid).copy(), field, newValue)
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
                val myMatches: ArrayList<MyMatch> = arrayListOf()
                viewLifecycleOwner.lifecycleScope.launch {
                    for (uid in fakeUser.matches!!) {
                        if (uid == fakeUser4.uid) {
                            myMatches.add(
                                MyMatch(
                                    getUser(uid).username!!,
                                    uid,
                                    false,
                                    true
                                )
                            )
                        } else {
                            myMatches.add(
                                MyMatch(
                                    getUser(uid).username!!,
                                    uid,
                                    false,
                                    false
                                )
                            )
                        }
                    }
                    setupAdapter(myMatches)
                }
            }
        }))
    }

    @Singleton
    @Provides
    open fun provideUserHelper(): UserHelper {
        val user = mock(UserHelper::class.java)
        Mockito.`when`(user.getEmail()).thenReturn(PRIMARY_EMAIL)

        val taskCompletionSource = TaskCompletionSource<Void>()
        Handler(Looper.getMainLooper()).postDelayed({ taskCompletionSource.setResult(null) }, 1000L)
        val successfulTask = taskCompletionSource.task

        Mockito.`when`(user.setEmail(SECOND_EMAIL)).thenReturn(successfulTask)

        Mockito.`when`(user.getUserId()).thenReturn(TEST_UID)

        user.stub {
            onBlocking { isNewUser() }.doReturn(false)
        }

        val fakeIntent = mock(Intent::class.java)
        Mockito.`when`(user.getSignInIntent()).thenReturn(fakeIntent)

        Mockito.`when`(user.isLoggedIn()).thenReturn(true)

        Mockito.`when`(user.logout(any())).thenReturn(successfulTask)
        Mockito.`when`(user.delete(any())).thenReturn(successfulTask)
        return user
    }
}
