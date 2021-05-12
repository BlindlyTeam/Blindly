package ch.epfl.sdp.blindly.fake_module

import ch.epfl.sdp.blindly.dependency_injection.WeatherServiceModule
import ch.epfl.sdp.blindly.weather.*
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.stubbing.Answer
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [WeatherServiceModule::class]
)
open class FakeWeatherServiceModule {
    companion object {
        val TEMPERATURE = DayTemperature(1.0, 2.0, 3.0, 4.0, TemperatureUnit.METRIC)
        val WEATHER = Weather("Very nice weather", "01d")
        const val DAY = "MON"
        val DAY_WEATHER = DayWeather(TEMPERATURE, arrayOf(WEATHER), DAY)
        val WEEK_WEATHER = WeekWeather(Array(7) {DAY_WEATHER})

        fun answerResult(weekWeather: WeekWeather): Answer<Unit> {
            return Answer<Unit> { invocationOnMock ->
                val callback: WeatherService.WeatherResultCallback = invocationOnMock.getArgument(
                    2,
                    WeatherService.WeatherResultCallback::class.java
                )
                callback.onWeatherResponse(weekWeather)
            }
        }
    }

    @Singleton
    @Provides
    open fun provideWeatherService(): WeatherService {
        val weeather = mock(WeatherService::class.java)
        `when`(weeather.nextWeek(any(), any(), any())).then(answerResult(WEEK_WEATHER))

        return weeather
    }
}