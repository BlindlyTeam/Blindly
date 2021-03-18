package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.utils.UserHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class MyAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myaccount)

        UserHelper.getEmail().also { findViewById<TextView>(R.id.email).text = it }
        findViewById<Button>(R.id.logout_button).setOnClickListener { UserHelper.signOut(this , object: OnCompleteListener<Void>{
            override fun onComplete(p0: Task<Void>) {
                if (p0.isComplete)
                    startActivity(Intent(this@MyAccountActivity, MainActivity::class.java))
                else
                    // TODO fixme
                    Toast.makeText(applicationContext, "LOGOUT ERROR", Toast.LENGTH_LONG).show()
            }
        }) }
    }

}
