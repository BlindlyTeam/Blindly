package ch.epfl.sdp.blindly.dependency_injection

import ch.epfl.sdp.blindly.weather.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * UserHelper Module fore dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object WeatherServiceModule {

    /**
     * Return a UserHelper to be injected
     *
     * @return UserHelper
     */
    @Singleton
    @Provides
    fun provideWeatherService(): WeatherService = WeatherService()
}