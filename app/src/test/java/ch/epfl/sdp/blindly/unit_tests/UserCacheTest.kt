package ch.epfl.sdp.blindly.unit_tests

import ch.epfl.sdp.blindly.location.AndroidLocationService.Companion.createLocationTableEPFL
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.storage.UserCache
import org.junit.Assert.*
import org.junit.Test

class UserCacheTest {
    companion object {
        private val TEST_UID = "DBrGTHNkj9Z3VaKIeQCJrL3FANg2"

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
        private const val recordingPath =
            "gs://blindly-24119.appspot.com/Recordings/DBrGTHNkj9Z3VaKIeQCJrL3FANg2-PresentationAudio.amr"

        val TEST_USER: User = User.Builder()
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
            .setRecordingPath(recordingPath)
            .build()
    }

    @Test
    fun emptyCacheReturnsNull() {
        val userCache = UserCache()
        assertEquals(null, userCache.get(TEST_UID))
    }

    @Test
    fun getAfterPutReturnNonNullUser() {
        val userCache = UserCache()

        userCache.put(TEST_UID, TEST_USER)
        assertEquals(TEST_USER, userCache.get(TEST_UID))
    }

    @Test
    fun containsReturnsFalseWhenUserIsNotCached() {
        val userCache = UserCache()
        assertFalse(userCache.contains(TEST_UID))
    }

    @Test
    fun containsReturnsTrueWhenUserIsCached() {
        val userCache = UserCache()
        userCache.put(TEST_UID, TEST_USER)
        assertTrue(userCache.contains(TEST_UID))
    }
}
