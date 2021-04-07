package ch.epfl.sdp.blindly.recording

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*

private const val NAME_AUDIO_ONE = "Audio 1"
private const val NAME_AUDIO_TWO = "Audio 2"
private const val DURATION_ZERO = "00:00"
private const val DURATION_ONE = "00:01"
private const val FILEPATH1 = "Test/myApp/file1"
private const val FILEPATH2 = "Test/myApp/file2"

@RunWith(AndroidJUnit4::class)
class AudioRecordTest {
    @Test
    fun equalsIsTrueWhithSameObjects() {
        val audio1 = AudioRecord(NAME_AUDIO_ONE, DURATION_ZERO, FILEPATH1, false)
        val audio2 = AudioRecord(NAME_AUDIO_ONE, DURATION_ZERO, FILEPATH1, false)
        assertThat(audio1 == audio2, equalTo(true))
    }

    @Test
    fun equalsIsFalseWithDifferentNames() {
        val audio1 = AudioRecord(NAME_AUDIO_ONE, DURATION_ZERO, FILEPATH1, false)
        val audio2 = AudioRecord(NAME_AUDIO_TWO, DURATION_ZERO, FILEPATH1, false)
        assertThat(audio1 == audio2, equalTo(false))
    }

    @Test
    fun equalsIsFalseWithDifferentDurations() {
        val audio1 = AudioRecord(NAME_AUDIO_ONE, DURATION_ZERO, FILEPATH1, false)
        val audio2 = AudioRecord(NAME_AUDIO_ONE, DURATION_ONE, FILEPATH1, false)
        assertThat(audio1 == audio2, equalTo(false))
    }

    @Test
    fun equalsIsFalseWithDifferentFilePaths() {
        val audio1 = AudioRecord(NAME_AUDIO_ONE, DURATION_ZERO, FILEPATH1, false)
        val audio2 = AudioRecord(NAME_AUDIO_ONE, DURATION_ZERO, FILEPATH2, false)
        assertThat(audio1 == audio2, equalTo(false))
    }

    @Test
    fun equalsIsFalseWithDifferentExpandedValue() {
        val audio1 = AudioRecord(NAME_AUDIO_ONE, DURATION_ZERO, FILEPATH1, false)
        val audio2 = AudioRecord(NAME_AUDIO_ONE, DURATION_ZERO, FILEPATH1, true)
        assertThat(audio1 == audio2, equalTo(false))
    }

    @Test
    fun hashCodeWorksWithSameObjects() {
        val audio1 = AudioRecord(NAME_AUDIO_ONE, DURATION_ZERO, FILEPATH1, false)
        val audio2 = AudioRecord(NAME_AUDIO_ONE, DURATION_ZERO, FILEPATH1, false)
        assertThat(audio1.hashCode() == audio2.hashCode(), equalTo(true))
    }

    @Test
    fun hashCodeWorksWithDifferentObjects() {
        val audio1 = AudioRecord(NAME_AUDIO_ONE, DURATION_ZERO, FILEPATH1, false)
        val audio2 = AudioRecord(NAME_AUDIO_TWO, DURATION_ONE, FILEPATH2, false)
        assertThat(audio1.hashCode() == audio2.hashCode(), equalTo(false))
    }

    @Test
    fun copyWorks() {
        val audio = AudioRecord(NAME_AUDIO_ONE, DURATION_ZERO, FILEPATH1, false)
        val audioCopy = audio.copy()
        assertThat(audio == audioCopy, equalTo(true))
    }
}