package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.my_matches.chat.Message
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Test

class MessagesTests {
    companion object {
        private const val TEXT_MESSAGE = "TEXT"
        private const val USER_ID = "user_id"
        private val LOCATION_MESSAGE = BlindlyLatLng(LAUSANNE_LATLNG)
    }

    @Test
    fun timestampSetToNow() {
        val msg = Message(TEXT_MESSAGE, USER_ID)
        val timestamp = System.currentTimeMillis()
        val delta = 500
        assertThat(
            msg.timestamp,
            allOf(
                greaterThanOrEqualTo(timestamp-delta),
                lessThanOrEqualTo(timestamp)))
    }
    @Test
    fun textMessageSetCorrectly() {
        val msg = Message(TEXT_MESSAGE, USER_ID)
        assertThat(msg.messageText, equalTo(TEXT_MESSAGE))
    }
    @Test
    fun locationMessageSetCorrectly() {
        val msg = Message(LOCATION_MESSAGE, USER_ID)
        assertThat(msg.messageText, equalTo(LOCATION_MESSAGE))
    }
    @Test
    fun emptyConstructor() {
        val msg = Message<BlindlyLatLng>()
        val timestamp = System.currentTimeMillis()
        msg.messageText = LOCATION_MESSAGE
        msg.currentUserId = USER_ID
        msg.timestamp = timestamp
        assertThat(msg.messageText, equalTo(LOCATION_MESSAGE))
        assertThat(msg.currentUserId, equalTo(USER_ID))
        assertThat(msg.timestamp, equalTo(timestamp))
    }
}
