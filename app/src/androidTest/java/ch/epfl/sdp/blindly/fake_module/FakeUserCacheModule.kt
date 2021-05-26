package ch.epfl.sdp.blindly.fake_module

import ch.epfl.sdp.blindly.dependency_injection.UserCacheModule
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID2
import ch.epfl.sdp.blindly.location.AndroidLocationService.Companion.createLocationTableEPFL
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.storage.UserCache
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
    companion object {
        private const val uid = TEST_UID
        private const val uid2 = TEST_UID2
        private const val username = "Jane Doe"
        private const val username2 = "Jack"
        private val location = createLocationTableEPFL() // Ecublens, Switzerland
        private const val MULHOUSE_LAT = 47.749
        private const val MULHOUSE_LON = 7.335
        private val location2 = listOf(MULHOUSE_LAT, MULHOUSE_LON) // Mulhouse, France
        private const val birthday = "01.01.2001"
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
        val userCache = Mockito.mock(UserCache::class.java)
        Mockito.`when`(userCache.get(TEST_UID)).thenReturn(fakeUser)
        Mockito.`when`(userCache.get(TEST_UID2)).thenReturn(fakeUser2)
        return userCache
    }
}