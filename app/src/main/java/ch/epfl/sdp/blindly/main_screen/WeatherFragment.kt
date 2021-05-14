package ch.epfl.sdp.blindly.main_screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
import ch.epfl.sdp.blindly.weather.WeatherActivity
import ch.epfl.sdp.blindly.weather.WeatherActivity.Companion.LOCATION

class WeatherFragment : Fragment() {

    companion object {
        private const val ARG_COUNT = "mapArgs"
        private var counter: Int? = null

        fun newInstance(counter: Int): WeatherFragment {
            val fragment = WeatherFragment()
            val args = Bundle()
            args.putInt(ARG_COUNT, counter)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            counter = requireArguments().getInt(ARG_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        val matchActivityButton = view.findViewById<Button>(R.id.open_weather)
        matchActivityButton.setOnClickListener { startWeather() }
        return view
    }

    private fun startWeather() {
        val intent = Intent(activity, WeatherActivity::class.java)
        intent.putExtra(LOCATION, BlindlyLatLng(LAUSANNE_LATLNG))
        startActivity(intent)
    }
}
