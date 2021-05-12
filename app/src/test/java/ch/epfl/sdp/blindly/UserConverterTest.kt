package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.database.localDB.UserConverter
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class UserConverterTest {

    private val converter = UserConverter()

    @Test
    fun locationConverter() {
        val locInit = listOf(22.5, 6.5)
        val str = converter.fromLocationToString(locInit)
        val locEnd = converter.fromStringToLocation(str)
        assertThat(locEnd, equalTo(locInit))
    }

    @Test
    fun stringListConverter() {
        val strListInit = listOf("a", "b", "c")
        val str = converter.fromStringListToString(strListInit)
        val strListEnd = converter.fromStringToStringList(str)
        assertThat(strListEnd, equalTo(strListInit))
    }

    @Test
    fun intListConverter() {
        val intListInit = listOf(18, 99)
        val str = converter.fromIntListToString(intListInit)
        val intListEnd = converter.fromStringToIntList(str)
        assertThat(intListEnd, equalTo(intListInit))
    }
}