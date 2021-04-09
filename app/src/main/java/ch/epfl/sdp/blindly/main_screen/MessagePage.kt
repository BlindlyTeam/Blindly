package ch.epfl.sdp.blindly.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindly.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagePage : Fragment() {

    companion object {
        private const val ARG_COUNT = "messageArgs";
        private var counter: Int? = null;

        fun newInstance(counter: Int): MessagePage {
            val fragment = MessagePage();
            val args = Bundle();
            args.putInt(ARG_COUNT, counter);
            fragment.arguments = args;
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            counter = requireArguments().getInt(ARG_COUNT)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set views
    }
}