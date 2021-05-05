package ch.epfl.sdp.blindly.unit_tests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import ch.epfl.sdp.blindly.match.Profile
import org.hamcrest.Matchers.equalTo
import org.junit.Test

class ProfileTest {
    private val andre1 = Profile("André", 25)
    private val andre2 = Profile("André", 24)
    private val pierre1 = Profile("Pierre", 25)
    private val pierre2 = Profile("Pierre", 24)

    @Test
    fun equalsIsTrueWithSameProfile() {
        assertThat(andre1 == andre1, equalTo(true))
    }

    @Test
    fun equalsIsFalseWithDifferentProfiles() {
        assertThat(andre1 == pierre2, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentNames() {
        assertThat(andre1 == pierre1, equalTo(false))
    }

    @Test
    fun equalsIsFalseForDifferentAges() {
        assertThat(andre2 == andre1, equalTo(false))
    }

    @Test
    fun hashCodeWorksWithSameObject() {
        assertThat(andre1.hashCode() == andre1.hashCode(), equalTo(true))
    }

    @Test
    fun hashCodeWorksWithDifferentProfiles() {
        assertThat(andre1.hashCode() == pierre1.hashCode(), equalTo(false))
    }

    @Test
    fun copyWorksForProfile() {
        val copyAndre = andre1.copy()
        assertThat(andre1 == copyAndre, equalTo(true))
    }

    @Test
    fun toStringDisplaysWell() {
        assertThat(andre1.toString() == "André, 25", equalTo(true))
    }

}