package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.location.AndroidLocationService.Companion.createLocationTableEPFL
import ch.epfl.sdp.blindly.user.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test

class UserUnitTest {
    companion object {
        private const val username = "Jane Doe"
        private const val username2 = "Alice and Bob"
        private val location = createLocationTableEPFL()
        private val location2 = listOf<Double>(1.0, 2.0)
        private const val birthday = "01.01.2001"
        private const val birthday2 = "01.02.1980"
        private const val gender = "Woman"
        private const val gender2 = "Men"
        private val sexualOrientations = listOf("Asexual")
        private val sexualOrientations2 = listOf("Pansexual")
        private const val showMe = "Everyone"
        private const val showMe2 = "Men"
        private val passions = listOf("Coffee", "Tea")
        private val passions2 = listOf("Tea")
        private const val radius = 150
        private const val radius2 = 15
        private val matches: List<String> = listOf("a1", "b2")
        private val matches2: List<String> = listOf("A3Verg34vrE3")
        private val likes: List<String> = listOf("c3", "d4")
        private val likes2: List<String> = listOf("efh14fjnaA")
        private const val description = "Student"
        private const val description2 = "Employee"
        private val ageRange = listOf(30, 40)
        private val ageRange2 = listOf(20, 60)
        private val recordingPath = "/user/Presentation.amr"
        private val recordingPath2 = "/user/PresentationNew.amr"

        private const val WRONG_INPUT_FOR_STRING = 5
        private const val WRONG_INPUT_FOR_LIST = "String"
        private val WRONG_INPUT_FOR_INT = listOf("Int")
        private val WRONG_INPUT_SIZE = listOf(45.6, 4, 5, 6)
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
        val userBuilder = User.Builder().setShowMe(showMe)
        assertThat(userBuilder.showMe, equalTo(showMe))
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
    fun setRecordingPathIsCorrect() {
        val userBuilder = User.Builder().setRecordingPath(recordingPath)
        assertThat(userBuilder.recordingPath, equalTo(recordingPath))
    }

    @Test
    fun buildBuilsCorrectUser() {
        val user = buildUser()

        assertThat(user.username, equalTo(username))
        assertThat(user.location, equalTo(location))
        assertThat(user.birthday, equalTo(birthday))
        assertThat(user.gender, equalTo(gender))
        assertThat(user.sexualOrientations, equalTo(sexualOrientations))
        assertThat(user.showMe, equalTo(showMe))
        assertThat(user.passions, equalTo(passions))
        assertThat(user.radius, equalTo(radius))
        assertThat(user.description, equalTo(description))
        assertThat(user.matches, equalTo(matches))
        assertThat(user.likes, equalTo(likes))
        assertThat(user.ageRange, equalTo(ageRange))
        assertThat(user.recordingPath, equalTo(recordingPath))
    }

    @Test
    fun getUserAgeIsCorrect() {
        val user = buildUser()
        val TEST_AGE = 20
        assertThat(User.getUserAge(user), equalTo(TEST_AGE))
    }

    @Test
    fun toStringIsCorrect() {
        val user = buildUser()

        assertThat(user.toString(), equalTo(username))
    }

    @Test
    fun updateUsernameIsCorrectWithAString() {
        val user = buildUser()
        User.updateUser(user, USERNAME, username2)
        assertThat(user.username, equalTo(username2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateNameWithOtherThanStringThrowsException() {
        val user = buildUser()
        User.updateUser(user, USERNAME, WRONG_INPUT_FOR_STRING)
    }

    @Test
    fun updateLocationIsCorrect() {
        val user = buildUser()
        User.updateUser(user, LOCATION, location2)
        assertThat(user.location, equalTo(location2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateLocationWithOtherThanListThrowsException() {
        val user = buildUser()
        User.updateUser(user, LOCATION, WRONG_INPUT_FOR_LIST)
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateLocationWithListWithSizeGreaterThanTwoThrowsException() {
        val user = buildUser()
        User.updateUser(user, LOCATION, WRONG_INPUT_SIZE)
    }

    @Test
    fun updateBirthdayIsCorrect() {
        val user = buildUser()
        User.updateUser(user, BIRTHDAY, birthday2)
        assertThat(user.birthday, equalTo(birthday2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateBirthdayWithOtherThanStringThrowsException() {
        val user = buildUser()
        User.updateUser(user, BIRTHDAY, WRONG_INPUT_FOR_STRING)
    }

    @Test
    fun updateGenderIsCorrect() {
        val user = buildUser()
        User.updateUser(user, GENDER, gender2)
        assertThat(user.gender, equalTo(gender2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateGenderWithOtherThanStringThrowsException() {
        val user = buildUser()
        User.updateUser(user, GENDER, WRONG_INPUT_FOR_STRING)
    }

    @Test
    fun updateSexualOrientationIsCorrect() {
        val user = buildUser()
        User.updateUser(user, SEXUAL_ORIENTATIONS, sexualOrientations2)
        assertThat(user.sexualOrientations, equalTo(sexualOrientations2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateSexualOrientationsWithOtherThanListThrowsException() {
        val user = buildUser()
        User.updateUser(user, SEXUAL_ORIENTATIONS, WRONG_INPUT_FOR_LIST)
    }

    @Test
    fun updateShowMeIsCorrect() {
        val user = buildUser()
        User.updateUser(user, SHOW_ME, showMe2)
        assertThat(user.showMe, equalTo(showMe2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateShowMeWithOtherThanStringThrowsException() {
        val user = buildUser()
        User.updateUser(user, SHOW_ME, WRONG_INPUT_FOR_STRING)
    }

    @Test
    fun updatePassionsIsCorrect() {
        val user = buildUser()
        User.updateUser(user, PASSIONS, passions2)
        assertThat(user.passions, equalTo(passions2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updatePassionsWithOtherThanListThrowsException() {
        val user = buildUser()
        User.updateUser(user, PASSIONS, WRONG_INPUT_FOR_LIST)
    }

    @Test
    fun updateRadiusIsCorrect() {
        val user = buildUser()
        User.updateUser(user, RADIUS, radius2)
        assertThat(user.radius, equalTo(radius2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateRadiusWithOtherThanIntThrowsException() {
        val user = buildUser()
        User.updateUser(user, RADIUS, WRONG_INPUT_FOR_INT)
    }

    @Test
    fun updateMatchesIsCorrect() {
        val user = buildUser()
        User.updateUser(user, MATCHES, matches2)
        assertThat(user.matches, equalTo(matches2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateMatchesWithOtherThanListThrowsException() {
        val user = buildUser()
        User.updateUser(user, MATCHES, WRONG_INPUT_FOR_LIST)
    }

    @Test
    fun updateLikesIsCorrect() {
        val user = buildUser()
        User.updateUser(user, LIKES, likes2)
        assertThat(user.likes, equalTo(likes2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateLikesWithOtherThanListThrowsException() {
        val user = buildUser()
        User.updateUser(user, LIKES, WRONG_INPUT_FOR_LIST)
    }


    @Test
    fun updateDescriptionIsCorrect() {
        val user = buildUser()
        User.updateUser(user, DESCRIPTION, description2)
        assertThat(user.description, equalTo(description2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateDescriptionWithOtherThanStringThrowsException() {
        val user = buildUser()
        User.updateUser(user, DESCRIPTION, WRONG_INPUT_FOR_STRING)
    }

    @Test
    fun updateRecordingPathIsCorrect() {
        val user = buildUser()
        User.updateUser(user, RECORDING_PATH, recordingPath2)
        assertThat(user.recordingPath, equalTo(recordingPath2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateRecordingPathWithOtherThanStringThrowsException() {
        val user = buildUser()
        User.updateUser(user, RECORDING_PATH, WRONG_INPUT_FOR_STRING)
    }

    @Test
    fun updateAgeRangeIsCorrect() {
        val user = buildUser()
        User.updateUser(user, AGE_RANGE, ageRange2)
        assertThat(user.ageRange, equalTo(ageRange2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateAgeRangeWithOtherThanListThrowsException() {
        val user = buildUser()
        User.updateUser(user, AGE_RANGE, WRONG_INPUT_FOR_LIST)
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateAgeRangeWithListWithSizeGreaterThanTwoThrowsException() {
        val user = buildUser()
        User.updateUser(user, AGE_RANGE, WRONG_INPUT_SIZE)
    }

    private fun buildUser(): User {
        return User.Builder()
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setDescription(description)
            .setMatches(matches)
            .setLikes(likes)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
    }
}
