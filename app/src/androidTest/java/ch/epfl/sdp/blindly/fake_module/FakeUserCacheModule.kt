package ch.epfl.sdp.blindly.fake_module

import ch.epfl.sdp.blindly.di.UserCacheModule
import ch.epfl.sdp.blindly.location.AndroidLocationService.Companion.createLocationEPFL
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
        private val loc = createLocationEPFL()
        private const val username = "Jane Doe"
        private val location = listOf(loc.latitude, loc.longitude)
        private const val birthday = "01.01.01"
        private const val gender = "Woman"
        private val sexualOrientations = listOf("Asexual")
        private const val showMe = "Everyone"
        private val passions = listOf("Coffee", "Tea")
        private const val radius = 150
        private val matches: List<String> = listOf()
        private const val description = "Student"
        private const val recordingPath = "Recordings/OKj1UxZao3hIVtma95gWZlner9p1-PresentationAudio.amr"
        private val ageRange = listOf(30, 50)
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
    }

    @Singleton
    @Provides
    open fun provideUserCache(): UserCache {
        val userCache = Mockito.mock(UserCache::class.java)
        Mockito.`when`(userCache.get(FakeUserHelperModule.TEST_UID)).thenReturn(fakeUser)
        return userCache
    }
}