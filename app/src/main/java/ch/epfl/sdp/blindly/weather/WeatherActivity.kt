package ch.epfl.sdp.blindly.weather

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class WeatherActivity : AppCompatActivity(), WeatherService.WeatherResultCallback,
    SwipeRefreshLayout.OnRefreshListener {
    companion object {
        const val LOCATION = "location"
    }

    @Inject
    lateinit var weather: WeatherService

    private lateinit var location: BlindlyLatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val refreshLayout = findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        refreshLayout.setOnRefreshListener(this)
        location = (intent.extras?.get(LOCATION) ?: BlindlyLatLng(LAUSANNE_LATLNG)) as BlindlyLatLng

        weather.nextWeek(location, callback = this)
        setRefreshing(true)
    }

    private fun temperatureToString(temperature: Double, unit: TemperatureUnit) =
        when (unit) {
            TemperatureUnit.IMPERIAL -> String.format(
                getString(R.string.fahrenheit_temperature),
                temperature.toInt()
            );
            TemperatureUnit.METRIC -> String.format(
                getString(R.string.celsius_temperature),
                temperature.toInt()
            );
        }

    /**
     * Maps the weather info to the view
     *
     * @param weather the weather to be shown
     */
    private fun setWeatherInfo(weather: WeekWeather) {
        // Skip today
        weather.daily.drop(1).forEachIndexed { index, dayWeather ->
            dayWeather.weather[0].getIconDrawableId()?.let { setIcon(index, it) }
            val unit = dayWeather.temperature.unit
            setDay(index, temperatureToString(dayWeather.temperature.day, unit))
            setEvening(index, temperatureToString(dayWeather.temperature.evening, unit))
            dayWeather.day?.let { setDayName(index, it) }
            setContainerVisibility(index, VISIBLE)
        }
    }

    private fun setIcon(index: Int, drawableId: Int) {
        val iconId = resources.getIdentifier(
            "weather_day_${(index + 1)}_icon",
            "id",
            packageName
        )
        findViewById<ImageView>(iconId)?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                drawableId
            )
        )
    }

    private fun setDay(index: Int, text: String) {
        val day = resources.getIdentifier(
            "weather_day_${(index + 1)}_day", "id",
            packageName
        )
        findViewById<TextView>(day)?.text = text
    }

    private fun setEvening(index: Int, text: String) {
        val evening = resources.getIdentifier(
            "weather_day_${(index + 1)}_evening",
            "id",
            packageName
        )
        findViewById<TextView>(evening)?.text = text
    }

    private fun setDayName(index: Int, text: String) {
        val dayName = resources.getIdentifier(
            "weather_day_${(index + 1)}_name",
            "id",
            packageName
        )
        findViewById<TextView>(dayName)?.text = text
    }

    private fun setContainerVisibility(index: Int, visibility: Int) {
        val containerId = resources.getIdentifier(
            "weather_day_${(index + 1)}",
            "id",
            packageName
        )
        findViewById<View>(containerId)?.visibility = visibility
    }

    // load a custom actionbar with a refresh button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.refresh, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.menu_refresh) {
            // We manually set refreshing so it's clearer that the app is doing a refresh
            setRefreshing(true)
            onRefresh()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onWeatherFailure(e: Exception) {
        val duration = Toast.LENGTH_LONG

        val toast = Toast.makeText(
            applicationContext,
            getString(R.string.weather_update_failed),
            duration
        )
        toast.show()
        setRefreshing(false)
    }

    override fun onWeatherResponse(weather: WeekWeather) {
        // As the request is performed on another thread we have to switch back to the UI thread to
        // perform the updates
        runOnUiThread {
            setWeatherInfo(weather)
            setRefreshing(false)
        }
    }

    private fun setRefreshing(isRefreshing: Boolean) {
        findViewById<SwipeRefreshLayout>(R.id.swiperefresh).isRefreshing = isRefreshing
    }

    /**
     * Refresh weather data when user swipes down
     *
     */
    override fun onRefresh() {
        weather.nextWeek(location, callback = this)
    }
}