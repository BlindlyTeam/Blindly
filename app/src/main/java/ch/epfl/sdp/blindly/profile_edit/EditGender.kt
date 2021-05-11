package ch.epfl.sdp.blindly.profile_edit

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.fragments.ProfilePageFragment
import ch.epfl.sdp.blindly.user.GENDER
import ch.epfl.sdp.blindly.user.enums.Gender.*

private val REGEX = Regex("^[a-zA-Z]*$")

class EditGender : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var gender: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_gender)
        supportActionBar?.hide()

        gender = intent.getStringExtra(GENDER).toString()
        val editGender = findViewById<EditText>(R.id.edit_gender)
        val updateGender = findViewById<Button>(R.id.update_gender_more)
        val edit = findViewById<Button>(R.id.edit_gender_button)
        radioGroup = findViewById(R.id.gender_radio_group)

        setCheckedRadioButton()

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val more = findViewById<RadioButton>(MORE.id)
            if (checkedId == MORE.id) {
                if(more.text == MORE.asString)
                    showGenderEditor()
                else
                    edit.visibility = VISIBLE
            } else {
                editGender.visibility = INVISIBLE
                edit.visibility = INVISIBLE
                updateGender.visibility = INVISIBLE
                hideAllWarnings()
            }
        }

        val bounce = AnimationUtils.loadAnimation(this, R.anim.bouncy_button)
        edit.setOnClickListener {
            edit.startAnimation(bounce)
            Handler(Looper.getMainLooper()).postDelayed({
                showGenderEditor()
                edit.visibility = View.INVISIBLE
            }, ProfilePageFragment.BOUNCE_DURATION)
        }

       setUpdateOnClickListener(updateGender)
    }

    override fun onBackPressed() {
        //TODO update in firestore if radioGroup.checkedChip.text != gender
        super.onBackPressed()
    }

    private fun setUpdateOnClickListener(updateGender: Button) {
        val more = findViewById<RadioButton>(MORE.id)
        val editGender = findViewById<EditText>(R.id.edit_gender)
        val editButton = findViewById<Button>(R.id.edit_gender_button)
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bouncy_button)
        updateGender.setOnClickListener {
            updateGender.startAnimation(bounce)
            Handler(Looper.getMainLooper()).postDelayed({
                hideAllWarnings()
                //TODO update in firestore
                if (genderMoreIsCorrect()) {
                    more.text = editGender.text
                    editGender.text.clear()
                    editGender.visibility = INVISIBLE
                    updateGender.visibility = INVISIBLE
                    editButton.visibility = VISIBLE
                }
            }, ProfilePageFragment.BOUNCE_DURATION)
        }
    }

    private fun showGenderEditor() {
        findViewById<EditText>(R.id.edit_gender).visibility = VISIBLE
        findViewById<Button>(R.id.update_gender_more).visibility = VISIBLE
    }

    private fun hideAllWarnings() {
        findViewById<TextView>(R.id.please_specify_warning).visibility = INVISIBLE
        findViewById<TextView>(R.id.use_only_letters_warning).visibility = INVISIBLE
    }

    private fun genderMoreIsCorrect(): Boolean {
        val editGender = findViewById<EditText>(R.id.edit_gender).text.toString()
        hideAllWarnings()

        return if (!editGender.matches(REGEX)) {
            //incorrect format, output error
            findViewById<TextView>(R.id.use_only_letters_warning).visibility = VISIBLE
            false
        } else {
            if (editGender.isNotEmpty()) {
                true
            } else {
                //empty text, output error
                findViewById<TextView>(R.id.please_specify_warning).visibility = VISIBLE
                false
            }
        }
    }

    private fun setCheckedRadioButton() {
        val more = findViewById<RadioButton>(MORE.id)
        when (gender) {
            WOMAN.asString -> {
                radioGroup.check(WOMAN.id)
                more.text = MORE.asString
            }
            MAN.asString -> {
                radioGroup.check(MAN.id)
                more.text = MORE.asString
            }
            else -> {
                radioGroup.check(MORE.id)
                more.text = gender
                findViewById<Button>(R.id.edit_gender_button).visibility = VISIBLE
            }
        }
    }
}