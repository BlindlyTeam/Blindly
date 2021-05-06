package ch.epfl.sdp.blindly

import android.location.Location
import ch.epfl.sdp.blindly.helpers.BlindlyLatLng
import ch.epfl.sdp.blindly.helpers.Message
import ch.epfl.sdp.blindly.location.AndroidLocationService.Companion.createLocationTableEPFL
import ch.epfl.sdp.blindly.settings.LAUSANNE_LATLNG
import ch.epfl.sdp.blindly.user.User
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Test

class MessagesTests {
    private val TEXT_MESSAGE = "TEXT"
    private val LOCATION_MESSAGE = BlindlyLatLng(LAUSANNE_LATLNG)
    private val USER_ID = "user_id"

    @Test
    fun timestampSetToNow() {
        val msg = Message<String>(TEXT_MESSAGE, USER_ID)
        val timestamp = System.currentTimeMillis()
        val delta = 500
        assertThat(
            msg.timestamp,
            allOf(
                greaterThanOrEqualTo(timestamp-delta),
                lessThanOrEqualTo(timestamp)));
    }
    @Test
    fun textMessageSetCorrectly() {
        val msg = Message<String>(TEXT_MESSAGE, USER_ID)
        assertEquals(TEXT_MESSAGE, msg.messageText)
    }
    @Test
    fun locationMessageSetCorrectly() {
        val msg = Message<BlindlyLatLng>(LOCATION_MESSAGE, USER_ID)
        assertEquals(LOCATION_MESSAGE, msg.messageText)
    }
    @Test
    fun emptyConstructor() {
        val msg = Message<BlindlyLatLng>()
        val timestamp = System.currentTimeMillis()
        msg.messageText = LOCATION_MESSAGE
        msg.currentUserId = USER_ID
        msg.timestamp = timestamp
        assertEquals(LOCATION_MESSAGE, msg.messageText)
        assertEquals(USER_ID, msg.currentUserId)
        assertEquals(timestamp, msg.timestamp)
    }
}
