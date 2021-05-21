package ch.epfl.sdp.blindly.weather

import android.content.Intent
import android.provider.CalendarContract
import android.view.View
import androidx.core.os.bundleOf
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.fake_module.FakeWeatherServiceModule
import ch.epfl.sdp.blindly.fake_module.FakeWeatherServiceModule.Companion.DAY
import ch.epfl.sdp.blindly.fake_module.FakeWeatherServiceModule.Companion.WEATHER
import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
import ch.epfl.sdp.blindly.weather.WeatherActivity.Companion.LOCATION
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.stubbing.Answer
import java.util.*
import javax.inject.Inject

private const val YEAR = 2021
private const val MONTH = 8
private const val DAY_OF_MONTH: Int = 8
private const val CALENDAR_EVENT_TITLE = "Blindly Date"
private const val TEMPERATURE_2 = 9999.0
private val DAY_TEMPERATURE_2 = DayTemperature(
    TEMPERATURE_2,
    TEMPERATURE_2,
    TEMPERATURE_2,
    TEMPERATURE_2,
    TemperatureUnit.METRIC
)
private val DAY_WEATHER_2 = DayWeather(DAY_TEMPERATURE_2, arrayOf(WEATHER), DAY)
private val WEEK_WEATHER_2 = WeekWeather(Array(7) { DAY_WEATHER_2 })

private val DAY_TEMPERATURE_FAHRENHEIT = DayTemperature(
    TEMPERATURE_2,
    TEMPERATURE_2,
    TEMPERATURE_2,
    TEMPERATURE_2,
    TemperatureUnit.IMPERIAL
)
private val DAY_WEATHER_FAHRENHEIT = DayWeather(DAY_TEMPERATURE_FAHRENHEIT, arrayOf(WEATHER), DAY)
private val WEEK_WEATHER_FAHRENHEIT = WeekWeather(Array(7) { DAY_WEATHER_FAHRENHEIT })

@HiltAndroidTest
class WeatherActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(
        WeatherActivity::class.java, bundleOf(
            Pair(
                LOCATION, BlindlyLatLng(
                    LAUSANNE_LATLNG
                )
            )
        )
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var weather: WeatherService
    private var decorView: View? = null

    @Before
    fun setup() {
        hiltRule.inject()
        Intents.init()
        activityRule.scenario.onActivity { activity ->
            activity.findViewById<SwipeRefreshLayout>(R.id.swiperefresh).isRefreshing = false
            decorView = activity.window.decorView
        }
    }

    @After
    fun afterEach() {
        Intents.release()
    }

    @Test
    fun setDayAndAddEvent() {
        val date = Calendar.getInstance()
        date.set(YEAR, MONTH, DAY_OF_MONTH)
        val calIntent = Intent(Intent.ACTION_INSERT)
        calIntent.data = CalendarContract.Events.CONTENT_URI
        calIntent.putExtra(CalendarContract.Events.TITLE, CALENDAR_EVENT_TITLE)
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
        calIntent.putExtra(
            CalendarContract.EXTRA_EVENT_BEGIN_TIME,
            date.timeInMillis
        )
        calIntent.putExtra(
            CalendarContract.EXTRA_EVENT_END_TIME,
            date.timeInMillis
        )

        onView(
            allOf(
                withId(R.id.eventButton),
                withParent(withId(R.id.weather_layout))
            )
        ).perform(click())
        intended(hasExtraWithKey(CalendarContract.Events.TITLE))
        intended(hasExtraWithKey(CalendarContract.EXTRA_EVENT_ALL_DAY))
        intended(hasExtraWithKey(CalendarContract.EXTRA_EVENT_BEGIN_TIME))
        intended(hasExtraWithKey(CalendarContract.EXTRA_EVENT_END_TIME))
    }

    @Test
    fun dragToRefreshFetchesNewDataAndDisplayThem() {
        // Override default result
        `when`(weather.nextWeek(any(), any(), any()))
            .then(FakeWeatherServiceModule.answerResult(WEEK_WEATHER_2))

        performRefresh()
        verifyMockCalledAgainAndViewUpdated()

    }

    @Test
    fun hideRefreshAfterResult() {
        `when`(weather.nextWeek(any(), any(), any()))
            .then(FakeWeatherServiceModule.answerResult(WEEK_WEATHER_2))

        performRefresh()
        verifyMockCalledAgainAndViewUpdated()
        assertNotRefreshing()
    }

    @Test
    fun unitsAreCorrectlyDisplayed() {
        onView(withId(R.id.weather_day_1_day))
            .check(
                matches(
                    withText(
                        // We only roughly check, it's up to the implementation to choose
                        // the precision of the display
                        containsString("°C")
                    )
                )
            )
        `when`(weather.nextWeek(any(), any(), any()))
            .then(FakeWeatherServiceModule.answerResult(WEEK_WEATHER_FAHRENHEIT))
        performRefresh()

        onView(withId(R.id.weather_day_1_day))
            .check(
                matches(
                    withText(
                        // We only roughly check, it's up to the implementation to choose
                        // the precision of the display
                        containsString("°F")
                    )
                )
            )
        assertNotRefreshing()
    }

    @Test
    fun hideRefreshAfterFailure() {
        `when`(weather.nextWeek(any(), any(), any()))
            .then((Answer<Unit> {
                // Ensure weather is fetched two times (at activity creation and refresh)
                val callback: WeatherService.WeatherResultCallback = it.getArgument(
                    2,
                    WeatherService.WeatherResultCallback::class.java
                )
                callback.onWeatherFailure(Exception("TEST EXCEPTION"))
            }))

        performRefresh()
        assertNotRefreshing()
    }

    private fun performRefresh() {
        onView(withId(R.id.swiperefresh)).perform(swipeDown())
    }

    private fun assertNotRefreshing() {
        activityRule.scenario.onActivity { act ->
            assertThat(
                act.findViewById<SwipeRefreshLayout>(R.id.swiperefresh).isRefreshing,
                equalTo(false)
            )
        }
    }

    private fun verifyMockCalledAgainAndViewUpdated() {
        // Ensure weather is fetched two times (at activity creation and refresh)
        verify(weather, times(2)).nextWeek(any(), any(), any())
        onView(withId(R.id.weather_day_1_day))
            .check(
                matches(
                    withText(
                        // We only roughly check, it's up to the implementation to choose
                        // the precision of the display
                        containsString(TEMPERATURE_2.toInt().toString())
                    )
                )
            )
    }
}


