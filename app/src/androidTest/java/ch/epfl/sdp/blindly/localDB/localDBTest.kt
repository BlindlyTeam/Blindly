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
        .setBirthday("29.04.1997")
        .setGender("female")
        .setSexualOrientations(listOf("male"))
        .setShowMe("male")
        .setAgeRange(listOf(20, 30))
        .setRadius(50)
        .build()

    val bob = userBuilder.setUsername("Bob")
        .setBirthday("06.01.1994")
        .setAgeRange(listOf(20, 30))
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
    fun putAndRetieveUser() {
        val a = UserEntity("alice", alice)
        userDAO.insertUser(a)
        val username = userDAO.getUserName("alice")
        assertThat(username, equalTo(alice.username))
    }
}