package ch.epfl.sdp.blindly.edit_info

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.audio_player.AudioPlayerFragment
import ch.epfl.sdp.blindly.settings.EXTRA_SHOW_ME
import ch.epfl.sdp.blindly.settings.SettingsShowMe
import ch.epfl.sdp.blindly.user.GENDER
import ch.epfl.sdp.blindly.user.PASSIONS
import ch.epfl.sdp.blindly.user.SEXUAL_ORIENTATIONS

/**
 * Activity that enables the user to edit his information (description, passions, etc)
 *
 */
class EditProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        supportActionBar?.hide()
    }

    fun startUsernameEdit(view: View) {
        val intent = Intent(this, EditUsername::class.java)
        startActivity(intent)
    }

    fun startGenderEdit(view: View) {
        val intent = Intent(this, EditGender::class.java)
        //TODO replace with gender from the database
        val gender = "Woman"
        intent.putExtra(GENDER, gender)
        startActivity(intent)
    }

    fun startSexualOrientationsEdit(view: View) {
        val intent = Intent(this, EditSexualOrientations::class.java)
        //TODO replace with sexual orientations form the database
        val sexualOrientations = arrayListOf(GAY, LESBIAN)
        intent.putStringArrayListExtra(SEXUAL_ORIENTATIONS, sexualOrientations)
        startActivity(intent)
    }

    fun startPassionsEdit(view: View) {
        val intent = Intent(this, EditPassions::class.java)
        //TODO replace with sexual orientations form the database
        val passions = arrayListOf(TEA, COFFEE)
        intent.putStringArrayListExtra(PASSIONS, passions)
        startActivity(intent)
    }
}