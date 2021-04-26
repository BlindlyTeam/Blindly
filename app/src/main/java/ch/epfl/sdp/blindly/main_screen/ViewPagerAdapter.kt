package ch.epfl.sdp.blindly.main_screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ch.epfl.sdp.blindly.main_screen.profile.ProfilePageFragment

/**
 * This ViewPagerAdapter is used by the ViewPager in the MainScreen
 * to instantiate the fragments
 */
class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    /**
     * Given a position, create the corresponding fragment
     */
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                MatchPageFragment.newInstance(position)
            }
            1 -> {
                MessagePageFragment.newInstance(position)
            }
            2 -> {
                ProfilePageFragment.newInstance(position)
            }
            else -> {
                MapFragment.newInstance(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}