package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.helpers.BlindlyLatLng
import ch.epfl.sdp.blindly.settings.LAUSANNE_LATLNG
import org.junit.Test

import ch.epfl.sdp.blindly.weather.WeatherService
import ch.epfl.sdp.blindly.weather.WeekWeather
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThanOrEqualTo
import java.lang.Exception
import java.util.concurrent.CompletableFuture

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
            WeatherService.WeatherResultCallback() {
            override fun onFailure(e: Exception) {
                future.completeExceptionally(e)
            }

            override fun onResponse(weather: WeekWeather) {
                println(weather)
                assertThat(weather.daily.size, greaterThanOrEqualTo(WEEK_LENGHT))
                weather.daily.forEach { assertThat(it.weather.size, greaterThanOrEqualTo(MIN_WEATHER_LENGTH)) }
                future.complete(FUTURE_COMPLETED)
            }
        })
        assertThat(future.get(), equalTo(FUTURE_COMPLETED))
    }
}