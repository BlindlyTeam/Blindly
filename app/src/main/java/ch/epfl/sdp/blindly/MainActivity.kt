package ch.epfl.sdp.blindly

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null)
            startActivity(Intent(this, SignInActivity::class.java))
        else
            println("111111")
            println(auth.currentUser?.email)
    }
}