package ch.epfl.sdp.blindly.main_screen.map

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindly.R

class MapFragment : Fragment() {

    companion object {
        private const val ARG_COUNT = "mapArgs"
        private var counter: Int? = null

        fun newInstance(counter: Int): MapFragment {
            val fragment = MapFragment()
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
        val view = inflater.inflate(R.layout.fragment_map_page, container, false)
        val matchActivityButton = view.findViewById<Button>(R.id.open_map)
        matchActivityButton.setOnClickListener { startUserMap() }
        return view
    }

    private fun startUserMap() {
        val intent = Intent(activity, UserMapActivity::class.java)
        // TODO : This is where we pass the UID of the matched User
        val bundle = bundleOf("matchedId" to "TBzoT5LxhdTwcDvvEz5JXIkpVx12")

        intent.putExtras(bundle)
        startActivity(intent)
    }
}
