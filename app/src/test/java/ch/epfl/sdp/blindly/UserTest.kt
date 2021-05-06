package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.location.AndroidLocationService.Companion.createLocationTableEPFL
import ch.epfl.sdp.blindly.user.User
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test

class UserTest {
    companion object {
        private const val username = "Jane Doe"
        private val location = createLocationTableEPFL()
        private const val birthday = "01.01.2001"
        private const val gender = "Woman"
        private val sexualOrientations = listOf("Asexual")
        private const val show_me = "Everyone"
        private val passions = listOf("Coffee", "Tea")
        private const val radius = 150
        private val matches: List<String> = listOf("a1", "b2")
        private val likes: List<String> = listOf("c3", "d4")
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
        val userBuilder = User.Builder().setSexualOrientations(sexualOrientations)
        assertThat(userBuilder.sexualOrientations, equalTo(sexualOrientations))
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
    fun setLikesIsCorrect() {
        val userBuilder = User.Builder().setLikes(likes)
        assertThat(userBuilder.likes, equalTo(likes))
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
        User.Builder().setAgeRange(listOf(12, 3, 4))
    }

    @Test(expected = IllegalArgumentException::class)
    fun setLocationWithLenDifferentFromTwoThrowsException() {
        User.Builder().setLocation(listOf(1.3, 3.1, 4.5))
    }

    @Test
    fun buildBuilsCorrectUser() {
        val user: User = User.Builder()
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(show_me)
            .setPassions(passions)
            .setRadius(radius)
            .setDescription(description)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .build()

        assertThat(user.username, equalTo(username))
        assertThat(user.location, equalTo(location))
        assertThat(user.birthday, equalTo(birthday))
        assertThat(user.gender, equalTo(gender))
        assertThat(user.sexualOrientations, equalTo(sexualOrientations))
        assertThat(user.showMe, equalTo(show_me))
        assertThat(user.passions, equalTo(passions))
        assertThat(user.radius, equalTo(radius))
        assertThat(user.description, equalTo(description))
        assertThat(user.matches, equalTo(matches))
        assertThat(user.likes, equalTo(likes))
        assertThat(user.ageRange, equalTo(ageRange))
    }

    @Test
    fun getUserAgeIsCorrect() {
        val user: User = User.Builder()
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(show_me)
            .setPassions(passions)
            .setRadius(radius)
            .setDescription(description)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .build()

        val TEST_AGE = 20
        assertThat(User.getUserAge(user), equalTo(TEST_AGE))
    }

    @Test
    fun toStringIsCorrect() {
        val user: User = User.Builder()
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(show_me)
            .setPassions(passions)
            .setRadius(radius)
            .setDescription(description)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .build()

        assertThat(user.toString(), equalTo(username))
    }
}
