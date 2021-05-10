package ch.epfl.sdp.blindly.localDB

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.user.User
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalDBTest {
    private lateinit var userDAO: UserDAO
    private lateinit var db: AppDatabase
    private val userBuilder = User.Builder()

    private val alice = userBuilder.setUsername("Alice")
        .setLocation(listOf(22.5, 5.6))
        .setBirthday("29.04.1997")
        .setGender("female")
        .setSexualOrientations(listOf("male"))
        .setShowMe("male")
        .setPassions(listOf("wine"))
        .setAgeRange(listOf(20, 30))
        .setRadius(50)
        .setLikes(listOf("bob"))
        .setMatches(listOf("bob"))
        .setRecordingPath("path")
        .setDescription("description")
        .build()

    /*
    val bob = userBuilder.setUsername("Bob")
        .setBirthday("06.01.1994")
        .setAgeRange(listOf(20, 30))
        .build()
        */


    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDAO = db.UserDAO()
    }

    @After
    @Throws(Exception::class)
    fun closeDB() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetieveUserUsername() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val username = userDAO.getUserName("alice")
        assertThat(username, equalTo("Alice"))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetieveUserLocation() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val location = userDAO.getUserLocation("alice")
        assertThat(location, equalTo(listOf(22.5, 5.6)))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetieveUserBirthday() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val birthday = userDAO.getUserBirthday("alice")
        assertThat(birthday, equalTo("29.04.1997"))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetieveUserGender() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val gender = userDAO.getUserGender("alice")
        assertThat(gender, equalTo("female"))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetieveUserShowMe() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val showMe = userDAO.getUserShowMe("alice")
        assertThat(showMe, equalTo("male"))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetieveUserPassions() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val passions = userDAO.getUserPassions("alice")
        assertThat(passions, equalTo(listOf("wine")))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetieveUserAgeRange() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val ageRange = userDAO.getUserAgeRange("alice")
        assertThat(ageRange, equalTo(listOf(20, 30)))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetieveUserRadius() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val radius = userDAO.getUserRadius("alice")
        assertThat(radius, equalTo(50))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetieveUserMatches() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val matches = userDAO.getUserMatches("alice")
        assertThat(matches, equalTo(listOf("bob")))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetieveUserLikes() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val likes = userDAO.getUserLikes("alice")
        assertThat(likes, equalTo(listOf("bob")))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetieveUserRecordingPath() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val recordingPath = userDAO.getUserRecordingPath("alice")
        assertThat(recordingPath, equalTo("path"))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetieveUserDescription() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val description = userDAO.getUserDescription("alice")
        assertThat(description, equalTo("description"))
    }
}