package ch.epfl.sdp.blindly.main_screen.match

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.main_screen.match.algorithm.UserListFilter
import ch.epfl.sdp.blindly.user.User
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith

private const val EPFL_LAT = 46.5222
private const val EPFL_LNG = 6.5660
private const val GENEVA_LAT = 46.2044
private const val GENEVA_LNG = 6.1432
private const val NYC_LAT = 40.7128
private const val NYC_LNG = 74.0060

@RunWith(AndroidJUnit4::class)
class UserListFilterUnitTest {
    private val userListFilter = UserListFilter()
    private val userBuilder = User.Builder()

    private val locationEPFL = listOf(EPFL_LAT, EPFL_LNG)
    private val locationNYC = listOf(NYC_LAT, NYC_LNG)
    private val locationGeneva = listOf(GENEVA_LAT, GENEVA_LNG)

    @Test
    fun filterLocationAndAgeRangeWorks() {
        val alice = userBuilder.setUsername("Alice")
            .setBirthday("29.04.1997")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationEPFL)
            .setRadius(50)
            .build()

        // No problem, Bob is perfect for Alice ! <3
        val bob = userBuilder.setUsername("Bob")
            .setBirthday("06.01.1994")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationEPFL)
            .build()

        // Age problem
        val cedric = userBuilder.setUsername("Cedric")
            .setBirthday("07.02.1971")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationEPFL)
            .build()

        // Location problem
        val eve = userBuilder.setUsername("Eve")
            .setBirthday("06.07.2004")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationNYC)
            .build()

        val userList = listOf(alice, bob, cedric, eve)
        val result = userListFilter.filterLocationAndAgeRange(alice, userList)
        assertThat(result, equalTo(listOf(alice, bob)))
    }

    @Test
    fun reversePotentialMatchWorks() {
        val alice = userBuilder.setUsername("Alice")
            .setGender("Woman")
            .setShowMe("Man")
            .setBirthday("29.04.1997")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationEPFL)
            .setRadius(80)
            .build()

        // No problem, Bob is perfect for Alice ! <3
        val bob = userBuilder.setUsername("Bob")
            .setGender("Man")
            .setShowMe("Woman")
            .setBirthday("06.01.1994")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationEPFL)
            .setRadius(80)
            .build()

        // Age range problem
        val cedric = userBuilder.setUsername("Cedric")
            .setGender("Man")
            .setShowMe("Woman")
            .setBirthday("07.02.2000")
            .setAgeRange(listOf(30, 40))
            .setLocation(locationEPFL)
            .setRadius(80)
            .build()

        // Location range problem
        val david = userBuilder.setUsername("David")
            .setGender("Man")
            .setShowMe("Woman")
            .setBirthday("29.04.1997")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationGeneva)
            .setRadius(40)
            .build()

        // Show me problem
        val emmanuel = userBuilder.setUsername("Emmanuel")
            .setGender("Man")
            .setShowMe("Man")
            .setBirthday("06.03.1998")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationEPFL)
            .setRadius(80)
            .build()

        // No problem, Francis is perfect for Alice ! <3
        val francis = userBuilder.setUsername("Francis")
            .setGender("Man")
            .setShowMe("Everyone")
            .setBirthday("06.01.1994")
            .setAgeRange(listOf(20, 30))
            .setLocation(locationEPFL)
            .setRadius(80)
            .build()

        val userList = listOf(alice, bob, cedric, david, emmanuel, francis)
        val result = userListFilter.reversePotentialMatch(alice, userList)
        assertThat(result, equalTo(listOf(bob, francis)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun exceptionIsThrownForNullLocation() {
        val alice = userBuilder.setUsername("Alice")
            .setGender("Woman")
            .setShowMe("Man")
            .setBirthday("29.04.1997")
            .setAgeRange(listOf(20, 30))
            .setRadius(80)
            .build()

        val userList = listOf(alice)
        userListFilter.filterLocationAndAgeRange(alice, userList)
    }
}