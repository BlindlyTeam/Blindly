package ch.epfl.sdp.blindly.fake_module

import android.content.Context
import androidx.room.Room
import ch.epfl.sdp.blindly.database.localDB.AppDatabase
import ch.epfl.sdp.blindly.database.localDB.UserDAO
import ch.epfl.sdp.blindly.database.localDB.UserEntity
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import ch.epfl.sdp.blindly.dependency_injection.AppDatabaseModule
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.user.User
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import org.mockito.Mockito
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppDatabaseModule::class]
)

open class FakeAppDatabaseModule {
    companion object {
        private const val uid = "abcd1234"
        private const val username = "Jane Doe"
        private val location =
            AndroidLocationService.createLocationTableEPFL() // Ecublens, Switzerland
        private const val birthday = "01.01.01"
        private const val gender = "Woman"
        private val sexualOrientations = listOf("Asexual")
        private const val showMe = "Everyone"
        private val passions = listOf("Coffee", "Tea")
        private const val radius = 150
        private val matches: List<String> = listOf("a1", "b2")
        private val likes: List<String> = listOf("c3", "d4")
        private const val recordingPath =
            "Recordings/OKj1UxZao3hIVtma95gWZlner9p1-PresentationAudio.amr"
        private val ageRange = listOf(30, 50)
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
    }

    @Singleton
    @Provides
    open fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "testDatabase").build()
    }

    @Singleton
    @Provides
    open fun provideUserDAO(database: AppDatabase): UserDAO {
        val userDAO = Mockito.mock(UserDAO::class.java)
        Mockito.`when`(userDAO.getUser(uid)).thenReturn(fakeUser)
        Mockito.`when`(userDAO.getUserUid(uid)).thenReturn(uid)
        Mockito.`when`(userDAO.getUserName(uid)).thenReturn(username)
        Mockito.`when`(userDAO.getUserLocation(uid)).thenReturn(location)
        Mockito.`when`(userDAO.getUserBirthday(uid)).thenReturn(birthday)
        Mockito.`when`(userDAO.getUserGender(uid)).thenReturn(gender)
        Mockito.`when`(userDAO.getUserSexualOrientation(uid)).thenReturn(sexualOrientations)
        Mockito.`when`(userDAO.getUserShowMe(uid)).thenReturn(showMe)
        Mockito.`when`(userDAO.getUserPassions(uid)).thenReturn(passions)
        Mockito.`when`(userDAO.getUserRadius(uid)).thenReturn(radius)
        Mockito.`when`(userDAO.getUserMatches(uid)).thenReturn(matches)
        Mockito.`when`(userDAO.getUserLikes(uid)).thenReturn(likes)
        Mockito.`when`(userDAO.getUserRecordingPath(uid)).thenReturn(recordingPath)
        Mockito.`when`(userDAO.getUserAgeRange(uid)).thenReturn(ageRange)
        Mockito.`when`(userDAO.deleteUser(UserEntity(uid, fakeUser))).thenReturn(1)
        Mockito.`when`(userDAO.getUserInfo(uid)).thenReturn(UserEntity(uid, fakeUser))
        Mockito.`when`(userDAO.updateUser(UserEntity(uid, fakeUser))).thenReturn(1)
        return userDAO
    }

}