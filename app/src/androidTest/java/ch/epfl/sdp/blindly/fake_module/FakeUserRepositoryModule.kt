package ch.epfl.sdp.blindly.fake_module

import ch.epfl.sdp.blindly.di.UserRepositoryModule
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserRepository
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
        replaces = [UserRepositoryModule::class]
)

open class FakeUserRepositoryModule {
    companion object {

        private const val username = "Jane Doe"
        private const val location = "EPFL, Ecublens"
        private const val birthday = "01.01.01"
        private const val gender = "Woman"
        private val  sexual_orientations =  listOf("Asexual")
        private const val show_me = "Everyone"
        private val passions = listOf("Coffee", "Tea")
        private const val radius = 150
        private val matches: List<User> = listOf()
        private const val description = "Student"
        private val fakeUser = User.Builder()
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
    }

    @Singleton
    @Provides
    open suspend fun provideUserRepository(): UserRepository {
        val userRepository = mock(UserRepository::class.java)
        Mockito.`when`(userRepository.getUser(FakeUserHelperModule.TEST_UID)).thenReturn(fakeUser)
        return userRepository
    }
}