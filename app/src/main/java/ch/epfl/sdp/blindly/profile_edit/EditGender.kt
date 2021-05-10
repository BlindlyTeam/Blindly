package ch.epfl.sdp.blindly.profile_edit

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.ProfilePageFragment
import ch.epfl.sdp.blindly.user.GENDER

private val REGEX = Regex("^[a-zA-Z]*$")

private const val WOMAN = "Woman"
private const val MAN = "Man"
private const val MORE = "More"
private const val WOMAN_ID = R.id.woman_radio_button
private const val MAN_ID = R.id.man_radio_button
private const val MORE_ID = R.id.more_radio_button

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
            val more = findViewById<RadioButton>(MORE_ID)
            if (checkedId == MORE_ID) {
                if(more.text == MORE)
                    showGenderEditor()
                else
                    edit.visibility = View.VISIBLE
            } else {
                editGender.visibility = View.INVISIBLE
                edit.visibility = View.INVISIBLE
                updateGender.visibility = View.INVISIBLE
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
        val more = findViewById<RadioButton>(MORE_ID)
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
                    editGender.visibility = View.INVISIBLE
                    updateGender.visibility = View.INVISIBLE
                    editButton.visibility = View.VISIBLE
                }
            }, ProfilePageFragment.BOUNCE_DURATION)
        }
    }

    private fun showGenderEditor() {
        findViewById<EditText>(R.id.edit_gender).visibility = View.VISIBLE
        findViewById<Button>(R.id.update_gender_more).visibility = View.VISIBLE
    }

    private fun hideAllWarnings() {
        findViewById<TextView>(R.id.warning1_p4_2).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning2_p4_2).visibility = View.INVISIBLE
    }

    private fun genderMoreIsCorrect(): Boolean {
        val editGender = findViewById<EditText>(R.id.edit_gender).text.toString()
        hideAllWarnings()

        return if (!editGender.matches(REGEX)) {
            //incorrect format, output error
            findViewById<TextView>(R.id.warning2_p4_2).visibility = View.VISIBLE
            false
        } else {
            val len = editGender.length
            if (len > 0) {
                true
            } else {
                //empty text, output error
                findViewById<TextView>(R.id.warning1_p4_2).visibility = View.VISIBLE
                false
            }
        }
    }

    private fun setCheckedRadioButton() {
        val more = findViewById<RadioButton>(MORE_ID)
        when (gender) {
            WOMAN -> {
                radioGroup.check(WOMAN_ID)
                more.text = MORE
            }
            MAN -> {
                radioGroup.check(MAN_ID)
                more.text = MORE
            }
            else -> {
                radioGroup.check(MORE_ID)
                more.text = gender
                findViewById<Button>(R.id.edit_gender_button).visibility = View.VISIBLE
            }
        }
    }
}