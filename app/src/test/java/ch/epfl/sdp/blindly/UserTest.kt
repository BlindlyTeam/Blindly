package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.location.AndroidLocationService.Companion.createLocationTableEPFL
import ch.epfl.sdp.blindly.user.*
import ch.epfl.sdp.blindly.user.User.Companion.toUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.core.Is
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.notNull

class UserTest {
    companion object {
        private const val uid = "abcd"
        private const val uid2 = "efgh"
        private const val username = "Jane Doe"
        private const val username2 = "Alice and Bob"
        private val location = createLocationTableEPFL()
        private val location2 = listOf(1.0, 2.0)
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
        private val dislikes : List<String> = listOf("c1", "c2")
        private val emptyLikes: List<String> = listOf()
        private val emptyDislikes: List<String> = listOf()
        private val emptyMatches: List<String> = listOf()
        private val likes2: List<String> = listOf("efh14fjnaA")
        private val ageRange = listOf(30, 40)
        private val ageRange2 = listOf(20, 60)
        private const val deleted = false
        private const val recordingPath = "/user/Presentation.amr"
        private const val recordingPath2 = "/user/PresentationNew.amr"

        private val reportingUsers = listOf("erdgae43ergag", "Aeadhyt34j")

        private const val WRONG_INPUT_FOR_STRING = 5
        private const val WRONG_INPUT_FOR_LIST = "String"
        private val WRONG_INPUT_FOR_LIST_STRING = listOf(5.0, 4)
        private val WRONG_INPUT_FOR_LIST_INT = listOf("Int")
        private val WRONG_INPUT_FOR_LIST_DOUBLE = listOf("Int")
        private val WRONG_INPUT_SIZE = listOf(45.6, 4, 5, 6)
        private val WRONG_INT_INPUT_SIZE = listOf(1, 2, 3)
        private val WRONG_INPUT_FOR_INT = listOf("Int")
        private const val WRONG_USERNAME_FIELD = "name"
        private val WRONG_INPUT_FOR_BOOLEAN = listOf(true)
    }

    @Test
    fun setUidIsCorrect() {
        val userBuilder = User.Builder().setUid(uid)
        assertThat(userBuilder.uid, equalTo(uid))
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
    fun setEmptyLikesListIsCorrect() {
        val userBuilder = User.Builder().setLikes(emptyLikes)
        assertThat(userBuilder.likes, equalTo(emptyLikes))
    }

    @Test
    fun setEmptyMatchesListIsCorrect() {
        val userBuilder = User.Builder().setLikes(emptyMatches)
        assertThat(userBuilder.matches, equalTo(emptyMatches))
    }

    @Test
    fun setEmptyDislikesListIsCorrect() {
        val userBuilder = User.Builder().setLikes(emptyDislikes)
        assertThat(userBuilder.dislikes, equalTo(emptyDislikes))
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
    fun buildBuildsCorrectUser() {
        val user = buildUser()

        assertThat(user.uid, equalTo(uid))
        assertThat(user.username, equalTo(username))
        assertThat(user.location, equalTo(location))
        assertThat(user.birthday, equalTo(birthday))
        assertThat(user.gender, equalTo(gender))
        assertThat(user.sexualOrientations, equalTo(sexualOrientations))
        assertThat(user.showMe, equalTo(showMe))
        assertThat(user.passions, equalTo(passions))
        assertThat(user.radius, equalTo(radius))
        assertThat(user.matches, equalTo(matches))
        assertThat(user.likes, equalTo(likes))
        assertThat(user.ageRange, equalTo(ageRange))
        assertThat(user.recordingPath, equalTo(recordingPath))
        assertThat(user.deleted, equalTo(false))
        assertThat(user.reportingUsers, equalTo(listOf()))
    }

    @Test
    fun getUserAgeIsCorrectWhenBirthdayIsNotNull() {
        val user = buildUser()
        val TEST_AGE = 20
        assertThat(User.getUserAge(user), equalTo(TEST_AGE))
    }

    @Test
    fun getUserAgeReturnsNullWhenBirthdayIsNull() {
        assertThat(User.getUserAge(null), nullValue())
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
    fun updateLocationWithOtherThanListOfDoubleThrowsException() {
        val user = buildUser()
        User.updateUser(user, LOCATION, WRONG_INPUT_FOR_LIST_DOUBLE)
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

    @Test(expected = IllegalArgumentException::class)
    fun updateSexualOrientationsWithOtherThanListOfStringThrowsException() {
        val user = buildUser()
        User.updateUser(user, SEXUAL_ORIENTATIONS, WRONG_INPUT_FOR_LIST_STRING)
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

    @Test(expected = IllegalArgumentException::class)
    fun updatePassionsWithOtherThanListOfStringThrowsException() {
        val user = buildUser()
        User.updateUser(user, PASSIONS, WRONG_INPUT_FOR_LIST_STRING)
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
    fun updateMatchesWithNonEmptyListIsCorrect() {
        val user = buildUser()
        User.updateUser(user, MATCHES, matches2)
        assertThat(user.matches, equalTo(matches2))
    }

    @Test
    fun updateMatchesWithEmptyListIsCorrect() {
        val user = buildUser()
        User.updateUser(user, MATCHES, emptyMatches)
        assertThat(user.matches, equalTo(listOf()))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateMatchesWithOtherThanListOfStringThrowsException() {
        val user = buildUser()
        User.updateUser(user, MATCHES, WRONG_INPUT_FOR_LIST_STRING)
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateDislikesWithOtherThanListOfStringThrowsException() {
        val user = buildUser()
        User.updateUser(user, DISLIKES, WRONG_INPUT_FOR_LIST_STRING)
    }

    @Test
    fun updateDislikesWithNonEmptyListIsCorrect() {
        val user = buildUser()
        User.updateUser(user, DISLIKES, dislikes)
        assertThat(user.dislikes, equalTo(dislikes))
    }

    @Test
    fun updateLikesWithNonEmptyListIsCorrect() {
        val user = buildUser()
        User.updateUser(user, LIKES, likes2)
        assertThat(user.likes, equalTo(likes2))
    }

    @Test
    fun updateLikesWithEmptyListIsCorrect() {
        val user = buildUser()
        User.updateUser(user, LIKES, emptyLikes)
        assertThat(user.likes, equalTo(listOf()))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateLikesWithOtherThanListOfStringThrowsException() {
        val user = buildUser()
        User.updateUser(user, LIKES, WRONG_INPUT_FOR_LIST_STRING)
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
    fun updateAgeRangeWithOtherThanListOfIntThrowsException() {
        val user = buildUser()
        User.updateUser(user, AGE_RANGE, WRONG_INPUT_FOR_LIST_INT)
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateAgeRangeWithListWithSizeGreaterThanTwoThrowsException() {
        val user = buildUser()
        User.updateUser(user, AGE_RANGE, WRONG_INT_INPUT_SIZE)
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateWithWrongFieldThrowsException() {
        val user = buildUser()
        User.updateUser(user, WRONG_USERNAME_FIELD, username2)
    }

    @Test
    fun equalsIsTrueForSameUser() {
        val user1 = buildUser()
        val user2 = buildUser()
        assertThat(user1 == user2, equalTo(true))
    }

    @Test
    fun equalsIsFalseForDifferentUids() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid2)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentUsernames() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid)
            .setUsername(username2)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentLocations() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location2)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentBirthdays() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday2)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentGenders() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender2)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentSexualOrientations() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations2)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentShowMe() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe2)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentPassions() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions2)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentRadius() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius2)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentMatches() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches2)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentLikes() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes2)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentAgeRanges() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange2)
            .setRecordingPath(recordingPath)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentRecordingPaths() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath2)
            .build()
        assertThat(user1 == user2, equalTo(false))
    }

    @Test
    fun hashCodeWorksForSameUser() {
        val user1 = buildUser()
        val user2 = buildUser()
        assertThat(user1.hashCode() == user2.hashCode(), equalTo(true))
    }

    @Test
    fun hashCodeWorksForDifferentUsers() {
        val user1 = buildUser()
        val user2 = User.Builder()
            .setUid(uid2)
            .setUsername(username2)
            .setLocation(location2)
            .setBirthday(birthday2)
            .setGender(gender2)
            .setSexualOrientations(sexualOrientations2)
            .setShowMe(showMe2)
            .setPassions(passions2)
            .setRadius(radius2)
            .setMatches(matches2)
            .setLikes(likes2)
            .setAgeRange(ageRange2)
            .setRecordingPath(recordingPath2)
            .build()
        assertThat(user1.hashCode() == user2.hashCode(), equalTo(false))
    }

    @Test
    fun copyWorks() {
        val user1 = buildUser()
        val user2 = user1.copy()
        assertThat(user1 == user2, equalTo(true))
    }

    @Test
    fun updateReportingUsersIsCorrect() {
        val user = buildUser()
        User.updateUser(user, REPORTING_USERS, reportingUsers)
        assertThat(user.reportingUsers, equalTo(reportingUsers))
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateReportingUsersWithOtherThanListThrowsException() {
        val user = buildUser()
        User.updateUser(user, REPORTING_USERS, WRONG_INPUT_FOR_LIST)
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateReportingUsersWithOtherThanListOfStringThrowsException() {
        val user = buildUser()
        User.updateUser(user, REPORTING_USERS, WRONG_INPUT_FOR_LIST_STRING)
    }

    @Test
    fun updateDeletedIsCorrect() {
        val user = buildUser()
        User.updateUser(user, DELETED, true)
        assertThat(user.deleted, equalTo(true))
    }

    @Test(expected = java.lang.IllegalArgumentException::class)
    fun updateDeleteWithWithOtherThanBooleanThrowsException() {
        val user = buildUser()
        User.updateUser(user, DELETED, WRONG_INPUT_FOR_BOOLEAN)
    }

    private fun buildUser(): User {
        return User.Builder()
            .setUid(uid)
            .setUsername(username)
            .setLocation(location)
            .setBirthday(birthday)
            .setGender(gender)
            .setSexualOrientations(sexualOrientations)
            .setShowMe(showMe)
            .setPassions(passions)
            .setRadius(radius)
            .setMatches(matches)
            .setLikes(likes)
            .setAgeRange(ageRange)
            .setRecordingPath(recordingPath)
            .build()
    }

    @Test
    fun testDocumentSnapshotTransform() {
        val ds = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(ds.id).thenReturn(uid)
        Mockito.`when`(ds.getString(USERNAME)).thenReturn(username)
        Mockito.`when`(ds.get(LOCATION)).thenReturn(location)
        Mockito.`when`(ds.getString(BIRTHDAY)).thenReturn(birthday)
        Mockito.`when`(ds.getString(GENDER)).thenReturn(gender)
        Mockito.`when`(ds.get(SEXUAL_ORIENTATIONS)).thenReturn(sexualOrientations)
        Mockito.`when`(ds.getString(SHOW_ME)).thenReturn(showMe)
        Mockito.`when`(ds.get(PASSIONS)).thenReturn(passions)
        Mockito.`when`(ds.getField<Int>(RADIUS)).thenReturn(radius)
        Mockito.`when`(ds.get(MATCHES)).thenReturn(matches)
        Mockito.`when`(ds.get(LIKES)).thenReturn(likes)
        Mockito.`when`(ds.get(AGE_RANGE)).thenReturn(ageRange)
        Mockito.`when`(ds.getString(RECORDING_PATH)).thenReturn(recordingPath)
        Mockito.`when`(ds.getField<Boolean>(DELETED)).thenReturn(deleted)
        Mockito.`when`(ds.get(REPORTING_USERS)).thenReturn(reportingUsers)


        val user = ds.toUser()
        assertThat(user, Is(notNullValue()))
        user!!
        assertThat(user.username, equalTo(username))
        assertThat(user.location, equalTo(location))
        assertThat(user.birthday, equalTo(birthday))
        assertThat(user.gender, equalTo(gender))
        assertThat(user.sexualOrientations, equalTo(sexualOrientations))
        assertThat(user.showMe, equalTo(showMe))
        assertThat(user.passions, equalTo(passions))
        assertThat(user.radius, equalTo(radius))
        assertThat(user.matches, equalTo(matches))
        assertThat(user.likes, equalTo(likes))
        assertThat(user.ageRange, equalTo(ageRange))
        assertThat(user.recordingPath, equalTo(recordingPath))
        assertThat(user.deleted, equalTo(deleted))
        assertThat(user.reportingUsers, equalTo(reportingUsers))
    }

    @Test
    fun testDocumentSnapshotTransformFail() {
        val ds = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(ds.id).thenReturn(uid)

        assertThat(ds.toUser(), `is`(nullValue()))
    }
}
