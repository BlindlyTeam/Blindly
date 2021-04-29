package ch.epfl.sdp.blindly

import android.location.Location
import ch.epfl.sdp.blindly.match.UserListFilter
import ch.epfl.sdp.blindly.user.User
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*

private const val EPFL_LAT = 46.5222
private const val EPFL_LNG = 6.5660
private const val NYC_LAT = 40.7128
private const val NYC_LNG = 74.0060

class UserListFilterUnitTest {
    private val userListFilter = UserListFilter()
    private val userBuilder = User.Builder()

    private val locationEPFL = createLocation(EPFL_LAT, EPFL_LNG)
    private val locationNYC = createLocation(NYC_LAT, NYC_LNG)

    @Test
    fun filterLocationAndAgeRangeWorks() {
        val alice = userBuilder.setUsername("Alice")
            .setBirthday("29.04.2007")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationEPFL)
            .setRadius(50)
            .build()
        val bob = userBuilder.setUsername("Bob")
            .setBirthday("06.01.2004")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationEPFL)
            .build()
        val cedric = userBuilder.setUsername("Eve")
            .setBirthday("07.02.1971")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationEPFL)
            .build()
        val eve = userBuilder.setUsername("Eve")
            .setBirthday("06.07.2004")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationNYC)
            .build()

        val userList = listOf(alice, bob, cedric, eve)
        val result = userListFilter.filterLocationAndAgeRange(alice, userList)
        assertThat(result, equalTo(listOf(bob)))
    }

    private fun createLocation(latitude: Double, longitude: Double) : Location {
        val location = Location("")
        location.latitude = latitude
        location.longitude = longitude
        return location
    }
}