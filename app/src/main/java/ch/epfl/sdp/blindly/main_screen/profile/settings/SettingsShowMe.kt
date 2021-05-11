package ch.epfl.sdp.blindly.main_screen.profile.settings

import android.os.Build
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.SHOW_ME
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserHelper.Companion.EXTRA_UID
import ch.epfl.sdp.blindly.viewmodel.UserViewModel
import ch.epfl.sdp.blindly.viewmodel.ViewModelAssistedFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val WOMEN = "Women"
const val MEN = "Men"
const val EVERYONE = "Everyone"

/**
 * Activity to modify the show me of the User
 */
@AndroidEntryPoint
class SettingsShowMe : AppCompatActivity() {

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    private lateinit var viewModel: UserViewModel

    private lateinit var currentShowMe: String
    private lateinit var showMe: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_show_me)

        instantiateViewModel()
        supportActionBar?.hide()

        showMe = intent.getStringExtra(EXTRA_SHOW_ME).toString()
        val showMeGroup = findViewById<RadioGroup>(R.id.show_me_radio_group)
        setClickedButton(showMeGroup, showMe)
        val showMeWomen = findViewById<RadioButton>(R.id.women_radio_button)
        setOnClickListener(showMeWomen, showMeGroup)
        val showMeMen = findViewById<RadioButton>(R.id.men_radio_button)
        setOnClickListener(showMeMen, showMeGroup)
        val showMeEveryone = findViewById<RadioButton>(R.id.everyone_radio_button)
        setOnClickListener(showMeEveryone, showMeGroup)

        viewModel.user.observe(this) {
            currentShowMe = it.showMe.toString()
        }
    }

    private fun setClickedButton(button: RadioGroup, showMe: String) {
        when (showMe) {
            WOMEN -> button.check(R.id.women_radio_button)
            MEN -> button.check(R.id.men_radio_button)
            EVERYONE -> button.check(R.id.everyone_radio_button)
        }
    }

    private fun setOnClickListener(button: RadioButton, radioGroup: RadioGroup) {
        button.setOnClickListener {
            currentShowMe = button.text.toString()
            setClickedButton(radioGroup, currentShowMe)
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBackPressed() {
        if (currentShowMe != showMe) {
            viewModel.updateField(SHOW_ME, currentShowMe)
        }
        super.onBackPressed()
    }

    private fun instantiateViewModel() {
        val bundle = Bundle()
        bundle.putString(EXTRA_UID, userHelper.getUserId())

        val viewModelFactory = assistedFactory.create(this, bundle)

        viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
    }
}