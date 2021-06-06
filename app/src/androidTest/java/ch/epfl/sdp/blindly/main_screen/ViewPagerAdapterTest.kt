package ch.epfl.sdp.blindly.main_screen

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.mockito.Mockito.mock

private const val COUNT = 3
private const val MATCH_POSITION = 0
private const val MY_MATCHES_POSITION = 1
private const val PROFILE_POSITION = 2
private const val MATCH_ARGS = "matchArgs"
private const val MY_MATHES_ARGS = "myMatchesArgs"
private const val PROFILE_ARGS = "profileArgs"

class ViewPagerAdapterUnitTest {
    private val fakeMainScreen = mock(MainScreen::class.java)
    private val viewPagerAdapter = ViewPagerAdapter(fakeMainScreen)

    @Test
    fun fragmentCountIsCorrect() {
        assertThat(viewPagerAdapter.itemCount, equalTo(COUNT))
    }

    @Test
    fun positionZeroCreatesMatchFragment() {
        assertThat(
            viewPagerAdapter.createFragment(MATCH_POSITION).arguments?.get(MATCH_ARGS), equalTo(
                MATCH_POSITION
            )
        )
    }

    @Test
    fun positionOneCreatesMessageFragment() {
        assertThat(
            viewPagerAdapter.createFragment(MY_MATCHES_POSITION).arguments?.get(MY_MATHES_ARGS),
            equalTo(
                MY_MATCHES_POSITION
            )
        )
    }

    @Test
    fun positionTwoCreatesProfileFragment() {
        assertThat(
            viewPagerAdapter.createFragment(PROFILE_POSITION).arguments?.get(PROFILE_ARGS), equalTo(
                PROFILE_POSITION
            )
        )
    }
}