package ch.epfl.sdp.blindly.main_screen.profile.edit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.GENDER
import ch.epfl.sdp.blindly.user.PASSIONS
import ch.epfl.sdp.blindly.user.SEXUAL_ORIENTATIONS
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.utils.CheckInternet
import ch.epfl.sdp.blindly.utils.ChipGroupUtils.Companion.setCheckedChips
import ch.epfl.sdp.blindly.viewmodel.UserViewModel
import ch.epfl.sdp.blindly.viewmodel.ViewModelAssistedFactory
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Activity that enables the user to edit his information (description, passions, etc)
 *
 */
@AndroidEntryPoint
class EditProfile : AppCompatActivity() {

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    private lateinit var viewModel: UserViewModel

    private var genderText: String? = null

    private var sexualOrientationsText: List<String>? = null
    private lateinit var sexualOrientations: ChipGroup
    private var passionsText: List<String>? = null
    private lateinit var passions: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val uid = userHelper.getUserId()
        viewModel = UserViewModel.instantiateViewModel(
            uid,
            assistedFactory,
            this,
            this
        )

        val username = findViewById<TextView>(R.id.username_text)
        val birthday = findViewById<TextView>(R.id.my_birthday)
        val gender = findViewById<TextView>(R.id.gender_text)
        sexualOrientations = findViewById(R.id.sexual_orientations_group)
        passions = findViewById(R.id.passions_group)

        viewModel.user.observe(this) { user ->
            username.text = user.username
            birthday.text = user.birthday
            gender.text = user.gender
            genderText = user.gender
            sexualOrientationsText = user.sexualOrientations
            sexualOrientationsText?.let { setCheckedChips(sexualOrientations, it, this) }
            passionsText = user.passions
            passionsText?.let { setCheckedChips(passions, it, this) }
        }

        if(!CheckInternet.internetIsConnected(this)) {
            Log.d(TAG, "Internet is not available: can't modify profile ")
            findViewById<Button>(R.id.username_button).isClickable = false
            findViewById<Button>(R.id.gender_button).isClickable = false
            findViewById<Button>(R.id.sexual_orientations_button).isClickable = false
            findViewById<Button>(R.id.passions_button).isClickable = false
        }
    }

    override fun onResume() {
        super.onResume()
        sexualOrientations.removeAllViews()
        passions.removeAllViews()
        viewModel.userUpdate()
    }

    fun startUsernameEdit(view: View) {
        val intent = Intent(this, EditUsername::class.java)
        startActivity(intent)
    }

    fun startGenderEdit(view: View) {
        val intent = Intent(this, EditGender::class.java)
        intent.putExtra(GENDER, genderText)
        startActivity(intent)
    }

    fun startSexualOrientationsEdit(view: View) {
        val intent = Intent(this, EditSexualOrientations::class.java)
        val sexualOrientations = arrayListOf<String>()
        sexualOrientationsText?.forEach {
            sexualOrientations.add(it)
        }
        intent.putStringArrayListExtra(
            SEXUAL_ORIENTATIONS,
            sexualOrientations
        )
        startActivity(intent)
    }

    fun startPassionsEdit(view: View) {
        val intent = Intent(this, EditPassions::class.java)
        val passions = arrayListOf<String>()
        passionsText?.forEach {
            passions.add(it)
        }
        intent.putStringArrayListExtra(PASSIONS, passions)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "EditProfile"
    }
}