package ch.epfl.sdp.blindly

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

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
}