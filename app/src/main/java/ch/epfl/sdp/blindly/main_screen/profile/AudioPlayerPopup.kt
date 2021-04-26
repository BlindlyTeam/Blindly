package ch.epfl.sdp.blindly.main_screen.profile

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.recording.AudioRecord
import ch.epfl.sdp.blindly.recording.RecordingActivity

class AudioPlayerPopup {

    /**
     * Show the audio_play PopUpWindow
     *
     * @param activity the profilePageFragment on which the PopUp shows up
     * @param view the view given from the ProfilePageFragment
     * @param context the context of the ProfilePageFragment
     */
    fun showPopUp(activity: ProfilePageFragment, view: View?, context: Context?) {
        // inflate the layout of the popup window
        val inflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        val popupView = initPopUpView(activity, inflater, context)
        val popupWindow = createPopUpWindow(popupView)

        // show the popup window
        if (view != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, view.bottom)
        }
    }

    /**
     * Initialize the popUpView and create the text and button for the audio_play layout
     *
     * @param activity an Instance of ProfilePageFragment, to launch a new activity from the popup
     * @param inflater a LayoutInflater
     * @param context the context of the ProfilePageFragment
     * @return a View to instantiate a PopUpWindow
     */
    private fun initPopUpView(activity: ProfilePageFragment, inflater : LayoutInflater?, context: Context?) : View? {
        val popupView: View? = inflater?.inflate(R.layout.audio_player, null)

        //TODO change this function with a ViewModel that observe this data
        val audioRecord = AudioRecord(
            "AudioRecord1",
            "00:36",
            "${context?.filesDir?.absolutePath}/TEMPaudioRecording_1.3gp",
            true
        )

        if(popupView != null) {
            val recordName = popupView.findViewById<TextView>(R.id.record_name)
            recordName.text = audioRecord.name

            val recordDuraton = popupView.findViewById<TextView>(R.id.record_duration)
            recordDuraton.text = audioRecord.durationText

            val playPauseButton = popupView.findViewById<ImageButton>(R.id.play_pause_button)

            val editButton = popupView.findViewById<Button>(R.id.edit_button)
            editButton.setOnClickListener {
                val intent = Intent(context, RecordingActivity::class.java)
                activity.startActivity(intent)
            }
        }

        return popupView
    }

    /**
     * Create the PopUpWindow
     *
     * @param popupView a PopUpView used to build the PopUpWindow
     * @return a PopUpWindow
     */
    private fun createPopUpWindow(popupView : View?): PopupWindow {
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it

        return PopupWindow(popupView, width, height, focusable)
    }
}