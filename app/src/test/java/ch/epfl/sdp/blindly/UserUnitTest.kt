package ch.epfl.sdp.blindly

import android.location.Location
import ch.epfl.sdp.blindly.fake_module.EPFL_LAT
import ch.epfl.sdp.blindly.fake_module.EPFL_LONG
import ch.epfl.sdp.blindly.user.User
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test

class UserUnitTest {
    companion object {
        private const val username = "Jane Doe"
        private val location = createLocationEPFL()
        private const val birthday = "01.01.2001"
        private const val gender = "Woman"
        private val  sexual_orientations = listOf("Asexual")
        private const val show_me = "Everyone"
        private val passions = listOf("Coffee", "Tea")
        private const val radius = 150
        private val matches: List<User> = listOf()
        private const val description = "Student"

        private fun createLocationEPFL(): Location {
            val location = Location("")
            location.latitude = EPFL_LAT
            location.longitude = EPFL_LONG
            return location
        }
    }

    @Test
    fun setUsernameIsCorrect() {
        val userBuilder = User.Builder().setUsername(username)
        assertThat(userBuilder.username, equalTo(username))
    }

    @Test
    fun setLocationIsCorrect() {
        val userBuilder = User.Builder().setLocation(location)
        assertThat(userBuilder.location, equalTo(location))
    }

    @Test
    fun setBirthdayIsCorrect() {
        val userBuilder = User.Builder().setBirthday(birthday)
        assertThat(userBuilder.birthday, equalTo(birthday))
    }

    @Test
    fun setGenderIsCorrect() {
        val userBuilder = User.Builder().setGender(gender)
        assertThat(userBuilder.gender, equalTo(gender))
    }

    @Test
    fun setSexualOrientationsIsCorrect() {
        val userBuilder = User.Builder().setSexualOrientations(sexual_orientations)
        assertThat(userBuilder.sexual_orientations, equalTo(sexual_orientations))
    }

    @Test
    fun setShowMeIsCorrect() {
        val userBuilder = User.Builder().setShowMe(show_me)
        assertThat(userBuilder.show_me, equalTo(show_me))
    }

    @Test
    fun setPassionsIsCorrect() {
        val userBuilder = User.Builder().setPassions(passions)
        assertThat(userBuilder.passions, equalTo(passions))
    }

    @Test
    fun setRadiusIsCorrect() {
        val userBuilder = User.Builder().setRadius(radius)
        assertThat(userBuilder.radius, equalTo(radius))
    }

    @Test
    fun setMatchesIsCorrect() {
        val userBuilder = User.Builder().setMatches(matches)
        assertThat(userBuilder.matches, equalTo(matches))
    }

    @Test
    fun setDescriptionIsCorrect() {
        val userBuilder = User.Builder().setDescription(description)
        assertThat(userBuilder.description, equalTo(description))
    }

    @Test
    fun buildBuilsCorrectUser() {
        val user : User = User.Builder()
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

        assertThat(user.username, equalTo(username))
        assertThat(user.location, equalTo(location))
        assertThat(user.birthday, equalTo(birthday))
        assertThat(user.gender, equalTo(gender))
        assertThat(user.sexual_orientations, equalTo(sexual_orientations))
        assertThat(user.show_me, equalTo(show_me))
        assertThat(user.passions, equalTo(passions))
        assertThat(user.radius, equalTo(radius))
        assertThat(user.description, equalTo(description))
        assertThat(user.matches, equalTo(matches))
    }

    @Test
    fun getUserAgeIsCorrect() {
        val user : User = User.Builder()
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

        val TEST_AGE = 20
        assertThat(User.getUserAge(user), equalTo(TEST_AGE))
    }

}