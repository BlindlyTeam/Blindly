package ch.epfl.sdp.blindly.weather

import ch.epfl.sdp.blindly.BuildConfig
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.location.BlindlyLatLng
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Various common temperature units
 *
 */
enum class TemperatureUnit {
    METRIC,
    IMPERIAL
}

/**
 * The temperatures at key moments of the day
 *
 *
 * @property day Temperature during the day
 * @property morning Temperature during the morning
 * @property evening Temperature during the evening
 * @property night Temperature during the night
 * @property unit the unit used for the temperatures
 */
// @Json is for the mapping to the json of the API calls
class DayTemperature(
    val day: Double,
    @Json(name = "morn") val morning: Double,
    @Json(name = "eve") val evening: Double,
    val night: Double,
    val unit: TemperatureUnit = TemperatureUnit.METRIC
) {
}

/**
 * The weather for a day
 *
 * @property temperature The temperatures for the day
 * @property weather the weather description
 */
open class DayWeather(
    @Json(name = "temp") val temperature: DayTemperature,
    val weather: Array<Weather>,
    val day: String?
) {
}

/**
 * The weather for the following week (7 days)
 *
 * @property daily the weather for a day
 */
open class WeekWeather(val daily: Array<out DayWeather>) {
}
/**
 * The weather during some time
 *
 * @param description the textual description of the weather
 */
class Weather(val description: String, private val icon: String) {
    companion object {
        private val ICON_MAP = mapOf(
            // Day icons
            "01d" to R.drawable.weather_01d,
            "02d" to R.drawable.weather_02d,
            "03d" to R.drawable.weather_03d,
            "04d" to R.drawable.weather_04d,
            "09d" to R.drawable.weather_09d,
            "10d" to R.drawable.weather_10d,
            "11d" to R.drawable.weather_11d,
            "13d" to R.drawable.weather_13d,
            "50d" to R.drawable.weather_50d,
            // night icons
            "01n" to R.drawable.weather_01n,
            "02n" to R.drawable.weather_02n,
            "03n" to R.drawable.weather_03n,
            "04n" to R.drawable.weather_04n,
            "09n" to R.drawable.weather_09n,
            "10n" to R.drawable.weather_10n,
            "11n" to R.drawable.weather_11n,
            "13n" to R.drawable.weather_13n,
            "50n" to R.drawable.weather_50n,
        )
    }

    fun getIconDrawableId() = ICON_MAP[icon]

}

/**
 * Service to get the weather
 *
 */
class WeatherService {
    private companion object {
        private const val url = BuildConfig.OPEN_WEATHER_MAP_URL
        private const val openWeatherMapAppId = BuildConfig.OPEN_WEATHER_MAP_KEY
        private const val DEFAULT_LANG = "en"
        private val SUPPORTED_LANGUAGES = arrayOf(
            "af",
            "al",
            "ar",
            "az",
            "bg",
            "ca",
            "cz",
            "da",
            "de",
            "el",
            "en",
            "eu",
            "fa",
            "fi",
            "fr",
            "gl",
            "he",
            "hi",
            "hr",
            "hu",
            "id",
            "it",
            "ja",
            "kr",
            "la",
            "lt",
            "mk",
            "no",
            "nl",
            "pl",
            "pt",
            "pt_br",
            "ro",
            "ru",
            "sv",
            "se",
            "sk",
            "sl",
            "sp",
            "es",
            "sr",
            "th",
            "tr",
            "ua",
            "uk",
            "vi",
            "zh_cn",
            "zh_tw",
            "zu"
        )
    }

    private var client: OkHttpClient = OkHttpClient()

    interface WeatherResultCallback {
        fun onWeatherFailure(e: Exception)
        fun onWeatherResponse(weather: WeekWeather)
    }

    private fun getLanguage(): String {
        var language = Locale.getDefault().displayLanguage
        if (SUPPORTED_LANGUAGES.contains(language))
            language = DEFAULT_LANG
        return language
    }

    private class OpenWeatherMapWeek(
        @Json(name = "timezone_offset") val timezoneOffset: Int,
        val daily: Array<OpenWeatherMapDayWeather>
    )
    private class OpenWeatherMapDayWeather(
        val dt: Long, @Json(name = "temp") temperature: DayTemperature,
        weather: Array<Weather>
    ) : DayWeather(
        temperature, weather, null
    )
    fun nextWeek(
        loc: BlindlyLatLng,
        language: String = getLanguage(),
        callback: WeatherResultCallback
    ) {
        val u: HttpUrl? =  url.toHttpUrlOrNull()
        // URL is either always valid or never valid as it is a build-time constant
        // so if the checks pass once it will always. There therefore is no need
        // to take action on runtime if it is invalid as it always will be
        val url = u!!.newBuilder()
            .addQueryParameter("appId", openWeatherMapAppId)
            .addQueryParameter("lang", language)
            .addQueryParameter("exclude", "current,minutely,hourly")
            .addQueryParameter("lat", loc.getLatitude().toString())
            .addQueryParameter("lon", loc.getLongitude().toString())
            .addQueryParameter("units", "metric")
        val request: Request = Request.Builder()
            .url(url.build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onWeatherFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val moshi: Moshi = Moshi.Builder()
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
                val jsonAdapter: JsonAdapter<OpenWeatherMapWeek> = moshi.adapter(
                    OpenWeatherMapWeek::class.java
                )
                val string = response.body?.string()
                if (string == null)
                    callback.onWeatherFailure(NullPointerException())
                else {
                    val result = jsonAdapter.fromJson(string)
                    if (result == null)
                        callback.onWeatherFailure(NullPointerException())
                    else {
                        val timezoneOffset = result.timezoneOffset
                        val mappedDaily = (result.daily as Array<OpenWeatherMapDayWeather>).map {
                            DayWeather(
                                it.temperature, it.weather, mapDate(
                                    timezoneOffset,
                                    it.dt
                                )
                            )
                        }.toTypedArray()
                        val mappedResult = WeekWeather(mappedDaily)
                        callback.onWeatherResponse(mappedResult)
                    }
                }
            }

        })
    }
    fun mapDate(timezoneOffset: Int, dt: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = dt * 1000
        val tz = TimeZone.getDefault().clone() as TimeZone
        tz.rawOffset = timezoneOffset * 1000
        cal.timeZone = tz

        val format1 = SimpleDateFormat("EEE")

        return format1.format(cal.time)
    }
}