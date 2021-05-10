package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.match.cards.Profile
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test

class ProfileTest {
    private val andre1 = Profile("André", 25, "Description André 1", "Wine, Coffee", "PathAndré1")
    private val andre2 = Profile("André", 24, "Description André 2", "Wine, Coffee", "PathAndré2")
    private val pierre1 =
        Profile("Pierre", 25, "Description Pierre 1", "Wine, Coffee", "PathPierre1")
    private val pierre2 =
        Profile("Pierre", 24, "Description Pierre 2", "Wine, Coffee", "PathPierre2")

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
        assertThat(andre1.toString() == "André, 25, Description André 1", equalTo(true))
    }
}