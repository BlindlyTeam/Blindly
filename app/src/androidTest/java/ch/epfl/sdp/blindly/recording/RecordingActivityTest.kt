package ch.epfl.sdp.blindly.recording

import android.content.pm.PackageManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Test
import org.junit.runner.RunWith

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

@RunWith(AndroidJUnit4::class)
class RecordingActivityTest {
    @Test
    fun onCreateWorks() {
        // TODO
    }

    @Test
    fun onRequestPermissionsResultGrantsPermissionCorrectly() {
        val recordingActivity = RecordingActivity()
        val permissions = arrayOf<String>()
        val grantResults = IntArray(2)
        recordingActivity.onRequestPermissionsResult(REQUEST_RECORD_AUDIO_PERMISSION, permissions,
                grantResults)
        assertThat(recordingActivity.permissionToRecordAccepted,
                equalTo(grantResults[0] == PackageManager.PERMISSION_GRANTED))
    }

    @Test
    fun onRequestPermissionResultDeniesPermissionCorrectly() {
        val recordingActivity = RecordingActivity()
        val permissions = arrayOf<String>()
        val grantResults = IntArray(2)
        recordingActivity.onRequestPermissionsResult(0, permissions,
                grantResults)
        assertThat(recordingActivity.permissionToRecordAccepted, equalTo(false))
    }

    @Test
    fun onStopWorks() {
        // TODO
    }

    @Test
    fun recordingAudioWorks() {

    }
}