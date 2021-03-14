package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment


class ProfilePage : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)

        val userInfoText = view.findViewById<TextView>(R.id.user_info_text)
        userInfoText.text = "Jane Doe, 24"

        val userDescriptionText = view.findViewById<TextView>(R.id.user_description_text)
        userDescriptionText.text = "Student"

        val settingsButton = view.findViewById<ImageButton>(R.id.settings_profile_button)
        settingsButton.setOnClickListener {
            val intent = Intent(this@ProfilePage.context, Settings::class.java)
            startActivity(intent)
        }

        val editButton = view.findViewById<ImageButton>(R.id.edit_info_profile_button)
        editButton.setOnClickListener {
            val intent = Intent(this@ProfilePage.context, EditProfile::class.java)
            startActivity(intent)
        }

        /*val recordAudioButton = view.findViewById<ImageButton>(R.id.record_audio_profile_button)
        settingsButton.setOnClickListener {
            val intent = Intent(this@ProfilePage.context, RecordAudio::class.java)
            startActivity(intent)
        }*/

        val audioLibraryButton = view.findViewById<Button>(R.id.audio_library_profile_button)
        audioLibraryButton.setOnClickListener {
            val intent = Intent(this@ProfilePage.context, AudioLibrary::class.java)
            startActivity(intent)
        }

        return view
    }
}