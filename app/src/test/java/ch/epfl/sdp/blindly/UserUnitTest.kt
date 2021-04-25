package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.location.AndroidLocationService.Companion.createLocationEPFL
import ch.epfl.sdp.blindly.user.User
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import java.lang.IllegalArgumentException

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
        private val ageRange = listOf(30, 40)
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
        assertThat(userBuilder.sexualOrientations, equalTo(sexual_orientations))
    }

    @Test
    fun setShowMeIsCorrect() {
        val userBuilder = User.Builder().setShowMe(show_me)
        assertThat(userBuilder.showMe, equalTo(show_me))
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
    fun setAgeRangeIsCorrect() {
        val userBuilder = User.Builder().setAgeRange(ageRange)
        assertThat(userBuilder.ageRange, equalTo(ageRange))
    }

    @Test(expected = IllegalArgumentException::class)
    fun setAgeRangeWithAgeRangeLenDifferentFromTwoThrowsException() {
        User.Builder().setAgeRange(listOf(12,3,4))
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
                .setAgeRange(ageRange)
                .build()

        assertThat(user.username, equalTo(username))
        assertThat(user.location, equalTo(location))
        assertThat(user.birthday, equalTo(birthday))
        assertThat(user.gender, equalTo(gender))
        assertThat(user.sexualOrientations, equalTo(sexual_orientations))
        assertThat(user.showMe, equalTo(show_me))
        assertThat(user.passions, equalTo(passions))
        assertThat(user.radius, equalTo(radius))
        assertThat(user.description, equalTo(description))
        assertThat(user.matches, equalTo(matches))
        assertThat(user.ageRange, equalTo(ageRange))
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
                .setAgeRange(ageRange)
                .build()

        val TEST_AGE = 20
        assertThat(User.getUserAge(user), equalTo(TEST_AGE))
    }

}