package ch.epfl.sdp.blindly.main_screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindly.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 * This activity holds the three fragments (Match, Message and Profile page)
 * and instantiate them
 */
private const val MATCH = "Match"
private const val MESSAGE = "Message"
private const val PROFILE = "Profile"

@AndroidEntryPoint
class MainScreen : AppCompatActivity() {
    private val tabTitles = arrayListOf(MATCH, MESSAGE, PROFILE)

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tabs)

        viewPager!!.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}
