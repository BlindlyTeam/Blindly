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
        .setSexualOrientations(listOf("bisexual"))
        .setShowMe("male")
        .setPassions(listOf("wine"))
        .setAgeRange(listOf(20, 30))
        .setRadius(50)
        .setLikes(listOf("bob"))
        .setMatches(listOf("bob"))
        .setRecordingPath("alice/path")
        .setDescription("description_alice")
        .build()


    private val bob = userBuilder.setUsername("Bob")
        .setLocation(listOf(22.5, 6.5))
        .setBirthday("06.01.1994")
        .setGender("male")
        .setSexualOrientations(listOf("gay"))
        .setShowMe("male")
        .setPassions(listOf("music"))
        .setAgeRange(listOf(20, 30))
        .setRadius(50)
        .setLikes(listOf("Mike"))
        .setMatches(listOf("Mike"))
        .setRecordingPath("bob/path")
        .setDescription("decription_bob")
        .build()



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
    fun putAndRetrieveUsersRadius() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val radius = userDAO.getUserRadius("alice")
        assertThat(radius, equalTo(50))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetrieveUsersMatches() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val matches = userDAO.getUserMatches("alice")
        assertThat(matches, equalTo(listOf("bob")))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetrieveUsersLikes() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val likes = userDAO.getUserLikes("alice")
        assertThat(likes, equalTo(listOf("bob")))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetrieveUsersRecordingPath() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val recordingPath = userDAO.getUserRecordingPath("alice")
        assertThat(recordingPath, equalTo("alice/path"))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetrieveUsersDescription() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val description = userDAO.getUserDescription("alice")
        assertThat(description, equalTo("description_alice"))
    }

    @Test
    @Throws(Exception::class)
    fun putAndRetrieveUser() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val user = userDAO.getUser("alice")
        assertThat(user!!.javaClass.toString(), equalTo(User::class.toString()))
    }

    @Test
    @Throws(Exception::class)
    fun putMultipleUsers() {
        val a = UserEntity("alice", alice)
        val b = UserEntity("bob", bob)
        userDAO.insertAllUsers(a, b)
        val first = userDAO.getUserName("alice")
        val second = userDAO.getUserName("bob")
        assertThat(listOf(first, second), equalTo(listOf("Alice", "Bob")))
    }

    @Test
    @Throws(Exception::class)
    fun putAndDeleteUser() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        userDAO.deleteUser(a)
        val user = userDAO.getUser("alice")
        assertThat(user, equalTo(null))
    }

    @Test
    @Throws(Exception::class)
    fun putAndUpdateUser() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val alice2 = alice
        alice2.radius = 80
        val a2 = UserEntity("alice", alice2)
        userDAO.updateUser(a2)
        val newRadius = userDAO.getUserRadius("alice")
        assertThat(newRadius, equalTo(80))
    }
}
