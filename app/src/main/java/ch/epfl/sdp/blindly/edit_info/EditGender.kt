package ch.epfl.sdp.blindly.edit_info

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_gender)
        supportActionBar?.hide()

        val gender = intent.getStringExtra(GENDER)
        val editGender = findViewById<EditText>(R.id.edit_gender)
        radioGroup = findViewById(R.id.gender_radio_group)

        if(gender != null)
            setCheckedRadioButton(gender)

        if (radioGroup.checkedRadioButtonId == MORE_ID)
            editGender.visibility = View.VISIBLE

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == MORE_ID) {
                editGender.visibility = View.VISIBLE
            } else {
                editGender.visibility = View.INVISIBLE
                findViewById<TextView>(R.id.warning1_p4_2).visibility = View.INVISIBLE
                findViewById<TextView>(R.id.warning2_p4_2).visibility = View.INVISIBLE
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
        findViewById<TextView>(R.id.warning1_p4_2).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning2_p4_2).visibility = View.INVISIBLE

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

    private fun setCheckedRadioButton(gender: String) {
        when(gender) {
            WOMAN -> radioGroup.check(WOMAN_ID)
            MAN -> radioGroup.check(MAN_ID)
            MORE -> radioGroup.check(MORE_ID)
        }
    }
}