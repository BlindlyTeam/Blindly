package ch.epfl.sdp.blindly.fake_module

import ch.epfl.sdp.blindly.di.UserCacheModule
import ch.epfl.sdp.blindly.location.AndroidLocationService.Companion.createLocationTableEPFL
import ch.epfl.sdp.blindly.user.User
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
    companion object {
        private const val username = "Jane Doe"
        private val location = createLocationTableEPFL() // Ecublens, Switzerland
        private val location2 = listOf(47.749, 7.335) // Mulhouse, France
        private const val birthday = "01.01.01"
        private const val gender = "Woman"
        private val sexualOrientations = listOf("Asexual")
        private const val showMe = "Everyone"
        private const val showMe2 = "Women"
        private val passions = listOf("Coffee", "Tea")
        private const val radius = 150
        private const val radius2 = 50
        private val matches: List<String> = listOf()
        private const val description = "Student"
        private const val recordingPath = "Recordings/OKj1UxZao3hIVtma95gWZlner9p1-PresentationAudio.amr"
        private val ageRange = listOf(30, 50)
        private val ageRange2 = listOf(40, 50)
        val fakeUser = User.Builder()
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setDescription(description)
            .setMatches(matches)
            .setRecordingPath(recordingPath)
            .setAgeRange(ageRange)
            .build()
        val fakeUser2 = User.Builder()
            .setUsername(username)
            .setLocation(location2)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe2)
            .setPassions(passions)
            .setRadius(radius2)
            .setDescription(description)
            .setMatches(matches)
            .setRecordingPath(recordingPath)
            .setAgeRange(ageRange2)
            .build()
    }

    @Singleton
    @Provides
    open fun provideUserCache(): UserCache {
        val userCache = Mockito.mock(UserCache::class.java)
        Mockito.`when`(userCache.get(FakeUserHelperModule.TEST_UID)).thenReturn(fakeUser)
        return userCache
    }
}