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
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppDatabaseModule::class]
)

open class FakeAppDatabaseModule {
    companion object {
        val fakeUser = FakeUserCacheModule.fakeUser
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
        Mockito.`when`(userDAO.getUser(any())).thenReturn(fakeUser)
        Mockito.`when`(userDAO.getUserUid(any())).thenReturn(fakeUser.uid)
        Mockito.`when`(userDAO.getUserName(any())).thenReturn(fakeUser.username)
        Mockito.`when`(userDAO.getUserLocation(any())).thenReturn(fakeUser.location)
        Mockito.`when`(userDAO.getUserBirthday(any())).thenReturn(fakeUser.birthday)
        Mockito.`when`(userDAO.getUserGender(any())).thenReturn(fakeUser.gender)
        Mockito.`when`(userDAO.getUserSexualOrientation(any())).thenReturn(fakeUser.sexualOrientations)
        Mockito.`when`(userDAO.getUserShowMe(any())).thenReturn(fakeUser.showMe)
        Mockito.`when`(userDAO.getUserPassions(any())).thenReturn(fakeUser.passions)
        Mockito.`when`(userDAO.getUserRadius(any())).thenReturn(fakeUser.radius)
        Mockito.`when`(userDAO.getUserMatches(any())).thenReturn(fakeUser.matches)
        Mockito.`when`(userDAO.getUserLikes(any())).thenReturn(fakeUser.likes)
        Mockito.`when`(userDAO.getUserRecordingPath(any())).thenReturn(fakeUser.recordingPath)
        Mockito.`when`(userDAO.getUserAgeRange(any())).thenReturn(fakeUser.ageRange)
        Mockito.`when`(userDAO.deleteUser(UserEntity(any(), fakeUser))).thenReturn(1)
        Mockito.`when`(userDAO.getUserInfo(any())).thenReturn(UserEntity(fakeUser.uid!!, fakeUser))
        Mockito.`when`(userDAO.updateUser(UserEntity(any(), fakeUser))).thenReturn(1)
        return userDAO
    }

}