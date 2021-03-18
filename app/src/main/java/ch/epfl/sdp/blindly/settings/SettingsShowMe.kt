package ch.epfl.sdp.blindly.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible
import ch.epfl.sdp.blindly.R

class SettingsShowMe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_show_me)

        supportActionBar?.hide()

        val showMeMen = findViewById<Button>(R.id.show_me_men_button)
        val showMeWomen = findViewById<Button>(R.id.show_me_women_button)
        val showMeEveryone = findViewById<Button>(R.id.show_me_everyone_button)
        val menCheckmark = findViewById<ImageView>(R.id.checkmark_img_men)
        val womenCheckmark = findViewById<ImageView>(R.id.checkmark_img_women)
        val everyoneCheckmark = findViewById<ImageView>(R.id.checkmark_img_everyone)
        val checks = listOf<ImageView>(menCheckmark, womenCheckmark, everyoneCheckmark)
        var currentShowMe = intent.getStringExtra(EXTRA_SHOW_ME)

        fun setOnClickListener(button: Button, newShowMe: String, checks:List<ImageView>) {
            button.setOnClickListener {
                if(newShowMe != currentShowMe) {
                    currentShowMe = newShowMe
                    displayCheckmark(currentShowMe!!, checks)
                }
            }
        }


        if (currentShowMe != null) {
            displayCheckmark(currentShowMe!!, checks)
            setOnClickListener(showMeMen, "Men", checks)
            setOnClickListener(showMeWomen, "Women", checks)
            setOnClickListener(showMeEveryone, "Everyone", checks)
        }

        val done = findViewById<Button>(R.id.done_button)
        done.setOnClickListener {
            val intent = Intent().apply {
                putExtra(EXTRA_SHOW_ME, currentShowMe)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

    }

    private fun displayCheckmark(showMe: String, checks:List<ImageView>) {
        val checkmark = when (showMe) {
            "Men" -> checks[0]
            "Women" -> checks[1]
            else -> checks[2]
        }
        checks.forEach { imageView ->
            if (imageView == checkmark) {
                checkmark.isVisible = true
            } else {
                imageView.isVisible = false
            }
        }
    }

}