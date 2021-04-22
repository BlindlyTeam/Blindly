package ch.epfl.sdp.blindly.fake_module

import android.location.Location
import ch.epfl.sdp.blindly.di.UserCacheModule
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserCache
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import javax.inject.Singleton

const val EPFL_LAT = 46.5
const val EPFL_LONG = 6.5

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
        private val  sexual_orientations =  listOf("Asexual")
        private const val show_me = "Everyone"
        private val passions = listOf("Coffee", "Tea")
        private const val radius = 150
        private val matches: List<User> = listOf()
        private const val description = "Student"
        val fakeUser = User.Builder()
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

        private fun createLocationEPFL(): Location {
            val location = Location("")
            location.latitude = EPFL_LAT
            location.longitude = EPFL_LONG
            return location
        }
    }

    @Singleton
    @Provides
    open fun provideUserCache(): UserCache {
        val userCache = Mockito.mock(UserCache::class.java)
        Mockito.`when`(userCache.get(FakeUserHelperModule.TEST_UID)).thenReturn(fakeUser)
        return userCache
    }

    private fun createLocationEPFL(): Location {
        val location = Location("")
        location.latitude = 46.5
        location.longitude = 6.5
        return location
    }
}