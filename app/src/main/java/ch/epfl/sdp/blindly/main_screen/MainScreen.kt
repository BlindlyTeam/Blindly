package ch.epfl.sdp.blindly.main_screen

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import ch.epfl.sdp.blindly.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

private const val MATCH = "Match"
private const val PROFILE = "Profile"
private const val MY_MATCHES = "My Matches"
private const val WEATHER = "Weather"
private const val EXIT_DIALOG_TITLE = "Exit the app."
private const val EXIT_DIALOG_MESSAGE = "Are You Sure?"
private const val ANSWER_YES = "Yes"
private const val ANSWER_NO = "No"

/**
 * This activity holds the three fragments (Match, Message and Profile page)
 * and instantiate them
 */
@AndroidEntryPoint
class MainScreen : AppCompatActivity() {

    private val tabIcons = arrayListOf(
        R.drawable.possible_matches_fragment_icon,
        R.drawable.my_matches_fragment_icon,
        R.drawable.profile_fragment_icon
    )


    var tabLayout: TabLayout? = null
    var viewPager: ViewPager2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tabs)

        viewPager!!.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.icon = ResourcesCompat.getDrawable(resources, tabIcons[position], null);
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
        builder.setTitle(EXIT_DIALOG_TITLE)
        builder.setMessage(EXIT_DIALOG_MESSAGE)
        builder.setPositiveButton(ANSWER_YES) { dialog, _ ->
            dialog.dismiss()
            this.finishAffinity()
        }
        builder.setNegativeButton(
            ANSWER_NO
        ) { dialog, _ -> dialog.dismiss() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}
