package ch.epfl.sdp.blindly.profile_edit

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.GENDER
import ch.epfl.sdp.blindly.user.enums.Gender.*

private val REGEX = Regex("^[a-zA-Z]*$")

class EditGender : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_gender)
        supportActionBar?.hide()

        val gender = intent.getStringExtra(GENDER)
        val editGender = findViewById<EditText>(R.id.edit_gender)
        radioGroup = findViewById(R.id.gender_radio_group)

        if(gender != null)
            setCheckedRadioButton(gender)

        if (radioGroup.checkedRadioButtonId == MORE.id)
            editGender.visibility = VISIBLE

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == MORE.id) {
                editGender.visibility = VISIBLE
            } else {
                editGender.visibility = INVISIBLE
                findViewById<TextView>(R.id.please_specify_warning).visibility = INVISIBLE
                findViewById<TextView>(R.id.use_only_letters_warning).visibility = INVISIBLE
            }
        }

    }

    override fun onBackPressed() {
        //Either Woman of Man is checked, no need to check for input
        if (radioGroup.checkedRadioButtonId != R.id.more_radio_button)
            super.onBackPressed()
        else
            if (genderMoreIsCorrect())
                super.onBackPressed()
    }

    private fun genderMoreIsCorrect(): Boolean {
        val editGender = findViewById<EditText>(R.id.edit_gender).text.toString()
        findViewById<TextView>(R.id.please_specify_warning).visibility = INVISIBLE
        findViewById<TextView>(R.id.use_only_letters_warning).visibility = INVISIBLE

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

    private fun setCheckedRadioButton(gender: String) {
        when(gender) {
            WOMAN.asString -> radioGroup.check(WOMAN.id)
            MAN.asString -> radioGroup.check(MAN.id)
            else -> radioGroup.check(MORE.id)
        }
    }
}