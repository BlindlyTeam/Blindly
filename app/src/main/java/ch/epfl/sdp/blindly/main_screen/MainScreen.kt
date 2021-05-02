package ch.epfl.sdp.blindly.main_screen

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
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
private const val MAP = "Map"


@AndroidEntryPoint
class MainScreen : AppCompatActivity() {
    private val tabTitles = arrayListOf(MATCH, MESSAGE, PROFILE, MAP)

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
        viewPager!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                viewPager!!.isUserInputEnabled =
                    !(state == SCROLL_STATE_DRAGGING && viewPager!!.currentItem == 0)
            }
        })
    }

    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Are You Sure?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            this.finishAffinity()
        }
        builder.setNegativeButton(
            "No"
        ) { dialog, _ -> dialog.dismiss() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }


}
