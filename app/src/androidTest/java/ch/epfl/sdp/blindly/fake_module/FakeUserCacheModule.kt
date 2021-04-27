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
        private const val username = "Jane Doe"
        private val location = createLocationEPFL()
        private const val birthday = "01.01.01"
        private const val gender = "Woman"
        private val sexual_orientations = listOf("Asexual")
        private const val show_me = "Everyone"
        private val passions = listOf("Coffee", "Tea")
        private const val radius = 150
        private val matches: List<User> = listOf()
        private const val description = "Student"
        private val ageRange = listOf(30, 50)
        val fakeUser = User.Builder()
<<<<<<< HEAD
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexual_orientations)
            .setShowMe(show_me)
            .setPassions(passions)
            .setRadius(radius)
            .setDescription(description)
            .setMatches(matches)
            .build()
=======
                .setUsername(username)
                .setLocation(location)
                .setBirthday(birthday)
                .setGender(gender)
                .setSexualOrientations(sexual_orientations)
                .setShowMe(show_me)
                .setPassions(passions)
                .setRadius(radius)
                .setDescription(description)
                .setMatches(matches)
                .setAgeRange(ageRange)
                .build()
>>>>>>> f05c67b9737a77d140e728bd037118c202b33f16
    }

    @Singleton
    @Provides
    open fun provideUserCache(): UserCache {
        val userCache = Mockito.mock(UserCache::class.java)
        Mockito.`when`(userCache.get(FakeUserHelperModule.TEST_UID)).thenReturn(fakeUser)
        return userCache
    }
}