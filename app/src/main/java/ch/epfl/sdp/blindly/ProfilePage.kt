package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

const val BOUNCE_DURATION : Long = 100
class ProfilePage : Fragment() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)

        val userInfoText = view.findViewById<TextView>(R.id.user_info_text)
        userInfoText.text = "Jane Doe, 24"

        val userDescriptionText = view.findViewById<TextView>(R.id.user_description_text)
        userDescriptionText.text = "Student"

        val context = this@ProfilePage.context
        val bounce = AnimationUtils.loadAnimation(context, R.anim.bouncy_button);

        val editButton = view.findViewById<Button>(R.id.edit_info_profile_button)
        editButton.setOnClickListener {
            val intent = Intent(context, EditProfile::class.java)
            editButton.startAnimation(bounce)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
            }, BOUNCE_DURATION)
        }

        /*val recordAudioButton = view.findViewById<Button>(R.id.record_audio_profile_button)
        settingsButton.setOnClickListener {
            val intent = Intent(this@ProfilePage.context, RecordAudio::class.java)
            startActivity(intent)
        }*/

        val settingsButton = view.findViewById<Button>(R.id.settings_profile_button)
        settingsButton.setOnClickListener {
            val intent = Intent(context, Settings::class.java)
            settingsButton.startAnimation(bounce)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
            }, BOUNCE_DURATION)
        }

        val audioLibraryButton = view.findViewById<Button>(R.id.audio_library_profile_button)
        audioLibraryButton.setOnClickListener {
            val intent = Intent(context, AudioLibrary::class.java)
            audioLibraryButton.startAnimation(bounce)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
            }, BOUNCE_DURATION)
        }

        return view
    }
}