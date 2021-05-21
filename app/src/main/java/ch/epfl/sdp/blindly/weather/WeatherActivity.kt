package ch.epfl.sdp.blindly.weather

import android.content.Intent
import android.content.Intent.ACTION_INSERT
import android.os.Bundle
import android.provider.CalendarContract.*
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class WeatherActivity : AppCompatActivity(), WeatherService.WeatherResultCallback,
    SwipeRefreshLayout.OnRefreshListener {
    companion object {
        const val LOCATION = "location"
        private const val WEATHER_PREFIX = "weather_day_"
        private const val CALENDAR_EVENT_TITLE = "Blindly Date"
        private const val DEF_TYPE = "id"
    }

    @Inject
    lateinit var weather: WeatherService

    private lateinit var location: BlindlyLatLng
    private lateinit var calendarView: CalendarView
    private var calendar = Calendar.getInstance()
    private val today = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val refreshLayout = findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        val eventButton = findViewById<Button>(R.id.eventButton)
        calendarView = findViewById(R.id.dateCalendarView)
        calendarView.firstDayOfWeek = Calendar.MONDAY

        // make today as the first clickable day
        calendarView.minDate = calendar.timeInMillis
        computeDayIndexAndSetBackground(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )

        // listen to changes to calendar and update weather images
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            computeDayIndexAndSetBackground(year, month, dayOfMonth)
        }

        //set calendar event if button is clicked
        eventButton.setOnClickListener {
            setCalendarEvent(CALENDAR_EVENT_TITLE, calendar)
        }

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
        weather.daily.forEachIndexed { index, dayWeather ->
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
            "${WEATHER_PREFIX}${(index + 1)}_icon",
            DEF_TYPE ,
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
            "${WEATHER_PREFIX}${(index + 1)}_day", "id",
            packageName
        )
        findViewById<TextView>(day)?.text = text
    }

    private fun setEvening(index: Int, text: String) {
        val evening = resources.getIdentifier(
            "${WEATHER_PREFIX}${(index + 1)}_evening",
            DEF_TYPE ,
            packageName
        )
        findViewById<TextView>(evening)?.text = text
    }

    private fun setDayName(index: Int, text: String) {
        val dayName = resources.getIdentifier(
            "${WEATHER_PREFIX}${(index + 1)}_name",
            DEF_TYPE ,
            packageName
        )
        findViewById<TextView>(dayName)?.text = text
    }

    private fun setContainerVisibility(index: Int, visibility: Int) {
        val containerId = resources.getIdentifier(
            "${WEATHER_PREFIX}${(index + 1)}",
            DEF_TYPE ,
            packageName
        )
        findViewById<View>(containerId)?.visibility = visibility
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

    /**
     * Forms a new Calendar Event in Google Calendar App,
     * closes the current activity as this page is no longer needed.
     *
     * @param title Title of the event to add to Calendar
     * @param date Date of the event to add to Calendar
     */
    private fun setCalendarEvent(title: String, date: Calendar) {
        val calIntent = Intent(ACTION_INSERT)
        calIntent.data = Events.CONTENT_URI
        calIntent.putExtra(Events.TITLE, title)
        calIntent.putExtra(EXTRA_EVENT_ALL_DAY, true)
        calIntent.putExtra(
            EXTRA_EVENT_BEGIN_TIME,
            date.timeInMillis
        )
        calIntent.putExtra(
            EXTRA_EVENT_END_TIME,
            date.timeInMillis
        )
        finish()
        startActivity(calIntent)
    }

    /**
     * Takes the new date, compares with today and gets the result as an index.
     * Then sends the result to setColoredBackgroundForSelectedDay to highlight
     * the weather of the chosen day
     *
     * @param year Year of selected date
     * @param month Month of selected date
     * @param dayOfMonth Day of selected date
     */
    private fun computeDayIndexAndSetBackground(year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        calendarView.date = calendar.timeInMillis
        val selectedDate = Calendar.getInstance()
        selectedDate.set(year, month, dayOfMonth)
        val diff: Long = selectedDate.timeInMillis - today.timeInMillis
        val dayIndex = TimeUnit.MILLISECONDS.toDays(diff)

        setColoredBackgroundForSelectedDay(dayIndex)
    }

    /**
     * If index is between the days we showed, (0 to 5 included, with 0 as today)
     * then highlight that day, otherwise set the background to white.
     *
     * @param indexOfDay Index of the day chosen (with 0 as today)
     */
    private fun setColoredBackgroundForSelectedDay(indexOfDay: Long) {
        for (i in 0..5) {
            val layoutId = resources.getIdentifier(
                "${WEATHER_PREFIX}${(i + 1)}",
                DEF_TYPE ,
                packageName
            )
            if (i.toLong() == indexOfDay) {

                findViewById<LinearLayout>(layoutId).setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.blindly_blue_transparent
                    )
                )
            } else {
                findViewById<LinearLayout>(layoutId).setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
            }
        }
    }

}

