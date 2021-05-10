package ch.epfl.sdp.blindly.profile_edit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.GENDER
import ch.epfl.sdp.blindly.user.PASSIONS
import ch.epfl.sdp.blindly.user.SEXUAL_ORIENTATIONS
import ch.epfl.sdp.blindly.user.enums.Passions
import ch.epfl.sdp.blindly.user.enums.Passions.*
import ch.epfl.sdp.blindly.user.enums.SexualOrientations
import ch.epfl.sdp.blindly.user.enums.SexualOrientations.*

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
        //TODO replace with sexual orientations from the database
        val sexualOrientations =
            arrayListOf(GAY.asString, LESBIAN.asString)
        intent.putStringArrayListExtra(SEXUAL_ORIENTATIONS, sexualOrientations)
        startActivity(intent)
    }

    fun startPassionsEdit(view: View) {
        val intent = Intent(this, EditPassions::class.java)
        //TODO replace with passions from the database
        val passions = arrayListOf(TEA.asString, COFFEE.asString)
        intent.putStringArrayListExtra(PASSIONS, passions)
        startActivity(intent)
    }
}