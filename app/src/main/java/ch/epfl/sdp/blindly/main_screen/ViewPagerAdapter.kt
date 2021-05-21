package ch.epfl.sdp.blindly.main_screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ch.epfl.sdp.blindly.main_screen.match.MatchPageFragment
import ch.epfl.sdp.blindly.main_screen.match.my_matches.MyMatchesFragment
import ch.epfl.sdp.blindly.main_screen.profile.ProfilePageFragment


/**
 * This ViewPagerAdapter is used by the ViewPager in the MainScreen
 * to instantiate the fragments
 */
class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    /**
     * Given a position, create the corresponding fragment
     *
     * @param position the position of the fragment to be instantiated in the TabLayout
     * @return the fragment that was created
     */
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                MatchPageFragment.newInstance(position)
            }

            1 -> {
                MyMatchesFragment.newInstance(position)
            }

            else -> {
                ProfilePageFragment.newInstance(position)
            }

        }

    }

    /**
     * Returns the number of fragments
     *
     * @return the number of fragments
     */
    override fun getItemCount(): Int {

        return 3

    }
}