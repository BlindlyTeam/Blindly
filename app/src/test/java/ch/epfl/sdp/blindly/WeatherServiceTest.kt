package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
import ch.epfl.sdp.blindly.weather.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Test
import java.util.concurrent.CompletableFuture

private const val day = 1.0
private const val morning = 2.0
private const val evening = 3.0
private const val night = 4.0

class WeatherServiceTest {
    companion object {
        const val FUTURE_COMPLETED = "Future completed"
        const val WEEK_LENGHT = 7
        const val MIN_WEATHER_LENGTH = 1
    }

    @Test
    fun callWorks() {
        val future: CompletableFuture<String> = CompletableFuture()
        WeatherService().nextWeek(BlindlyLatLng(LAUSANNE_LATLNG), callback = object :
            WeatherService.WeatherResultCallback {
            override fun onWeatherFailure(e: Exception) {
                future.completeExceptionally(e)
            }

            override fun onWeatherResponse(weather: WeekWeather) {
                println(weather)
                assertThat(weather.daily.size, greaterThanOrEqualTo(WEEK_LENGHT))
                weather.daily.forEach {
                    assertThat(
                        it.weather.size,
                        greaterThanOrEqualTo(MIN_WEATHER_LENGTH)
                    )
                }
                future.complete(FUTURE_COMPLETED)
            }
        })
        assertThat(future.get(), equalTo(FUTURE_COMPLETED))
    }

    @Test
    fun getDayWorks() {
        val dayTemp = DayTemperature(day, morning, evening, night)

        assertThat(dayTemp.day, equalTo(day))
        assertThat(dayTemp.morning, equalTo(morning))
        assertThat(dayTemp.evening, equalTo(evening))
        assertThat(dayTemp.night, equalTo(night))
        assertThat(dayTemp.unit, equalTo(TemperatureUnit.METRIC))
    }

    @Test
    fun commonWeatherIcons() {
        val icons = arrayOf(
            "01d",
            "02d",
            "03d",
            "04d",
            "09d",
            "10d",
            "11d",
            "13d",
            "50d",

            "01n",
            "02n",
            "03n",
            "04n",
            "09n",
            "10n",
            "11n",
            "13n",
            "50n"
        )

        icons.forEach {
            val icon = Weather("Weather", it).getIconDrawableId()
            assertThat(icon, not(equalTo(0)))
            assertThat(icon, not(nullValue()))
        }
    }

    @Test
    fun testDayWeather() {
        val dayTemp = DayTemperature(day, morning, evening, night)
        assertThat(DayWeather(dayTemp, arrayOf(), "MER").temperature, equalTo(dayTemp))
    }
}