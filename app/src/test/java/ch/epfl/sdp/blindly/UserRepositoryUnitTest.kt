package ch.epfl.sdp.blindly

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserCache
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

class UserRepositoryUnitTest {
    //TODO Though I'm not sure this is useful to test since the mock database will just return the fakeUser
/*
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db : FirebaseFirestore
    @Inject
    lateinit var userCache: UserCache

    @Before
    fun setup() {
        hiltRule.inject()
    }

    companion object {
        const val TEST_UID = "DBrGTHNkj9Z3VaKIeQCJrL3FANg2"
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
    }

    @Test
    suspend fun getUserReturnsTheUserWithTheRightUid() {
        val userRepository = UserRepository(db, userCache)
        assertEquals(fakeUser, userRepository.getUser(TEST_UID))
    }
*/
}
