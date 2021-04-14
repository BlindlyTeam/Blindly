package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.user.User
import org.junit.Test
import org.junit.Assert.*

class UserUnitTest {
    companion object {
        private const val username = "Jane Doe"
        private const val location = "EPFL, Ecublens"
        private const val birthday = "01.01.2001"
        private const val gender = "Woman"
        private val  sexual_orientations =  listOf("Asexual")
        private const val show_me = "Everyone"
        private val passions = listOf("Coffee", "Tea")
        private const val radius = 150
        private val matches: List<User> = listOf()
        private const val description = "Student"
    }

    @Test
    fun setUsernameIsCorrect() {
        val userBuilder = User.Builder().setUsername(username)
        assertEquals(username, userBuilder.username)
    }

    @Test
    fun setLocationIsCorrect() {
        val userBuilder = User.Builder().setLocation(location)
        assertEquals(location, userBuilder.location)
    }

    @Test
    fun setBirthdayIsCorrect() {
        val userBuilder = User.Builder().setBirthday(birthday)
        assertEquals(birthday, userBuilder.birthday)
    }

    @Test
    fun setGenderIsCorrect() {
        val userBuilder = User.Builder().setGender(gender)
        assertEquals(gender, userBuilder.gender)
    }

    @Test
    fun setSexualOrientationsIsCorrect() {
        val userBuilder = User.Builder().setSexualOrientations(sexual_orientations)
        assertEquals(sexual_orientations, userBuilder.sexual_orientations)
    }

    @Test
    fun setShowMeIsCorrect() {
        val userBuilder = User.Builder().setShowMe(show_me)
        assertEquals(show_me, userBuilder.show_me)
    }

    @Test
    fun setPassionsIsCorrect() {
        val userBuilder = User.Builder().setPassions(passions)
        assertEquals(passions, userBuilder.passions)
    }

    @Test
    fun setRadiusIsCorrect() {
        val userBuilder = User.Builder().setRadius(radius)
        assertEquals(radius, userBuilder.radius)
    }

    @Test
    fun setMatchesIsCorrect() {
        val userBuilder = User.Builder().setMatches(matches)
        assertEquals(matches, userBuilder.matches)
    }

    @Test
    fun setDescriptionIsCorrect() {
        val userBuilder = User.Builder().setDescription(description)
        assertEquals(description, userBuilder.description)
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

        assertEquals(username, user.username)
        assertEquals(location, user.location)
        assertEquals(birthday, user.birthday)
        assertEquals(gender, user.gender)
        assertEquals(sexual_orientations, user.sexual_orientations)
        assertEquals(show_me, user.show_me)
        assertEquals(passions, user.passions)
        assertEquals(radius, user.radius)
        assertEquals(description, user.description)
        assertEquals(matches, user.matches)
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

        val TEST_AGE = "20"
        assertEquals(TEST_AGE, User.getUserAge(user))
    }

}