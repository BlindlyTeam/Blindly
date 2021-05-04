package ch.epfl.sdp.blindly.settings

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.ViewModelAssistedFactory
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Activity to modify the show me of the User
 *
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

        val showMeGroup = findViewById<RadioGroup>(R.id.show_me_radio_group)
        val showMeWomen = findViewById<RadioButton>(R.id.women_radio_button)
        setOnClickListener(showMeWomen)
        val showMeMen = findViewById<RadioButton>(R.id.men_radio_button)
        setOnClickListener(showMeMen)
        val showMeEveryone = findViewById<RadioButton>(R.id.everyone_radio_button)
        setOnClickListener(showMeEveryone)

        currentShowMe = intent.getStringExtra(EXTRA_SHOW_ME).toString()
        viewModel.showMe.observe(this) {
            when (showMe) {
                "Women" -> showMeGroup.check(R.id.women_radio_button)
                "Men" -> showMeGroup.check(R.id.men_radio_button)
                "Everyone" -> showMeGroup.check(R.id.everyone_radio_button)
            }

        }


    }

    fun setOnClickListener(button: RadioButton) {
        button.setOnClickListener {
            showMe = button.text.toString()
        }
    }

    /*
    override fun onBackPressed() {
        if(showMe != currentShowMe) {

        }
        super.onBackPressed()
    }

     */

    private fun instantiateViewModel() {
        val bundle = Bundle()
        bundle.putString("uid", userHelper.getUserId())

        val viewModelFactory = assistedFactory.create(this, bundle)

        viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
    }
}