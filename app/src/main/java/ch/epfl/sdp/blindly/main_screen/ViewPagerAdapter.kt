package ch.epfl.sdp.blindly.main_screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                MatchPage.newInstance(position)
            }
            1 -> {
                MessagePage.newInstance(position)
            }
            else -> {
                ProfilePage.newInstance(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}