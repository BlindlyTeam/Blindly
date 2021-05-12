package ch.epfl.sdp.blindly.main_screen.my_matches

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.main_screen.match.my_matches.MyMatch
import ch.epfl.sdp.blindly.main_screen.match.my_matches.MyMatchesAdapter
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.Test
import org.mockito.Mockito.mock

@HiltAndroidTest
class MyMatchesAdapterTest {
    companion object {
        private const val NAME_1 = "user1"
        private const val UID_1 = "uid1"
        private const val IS_EXPANDED = false
        private const val NAME_2 = "user2"
        private const val UID_2 = "uid2"

    }

    @Test
    fun recyclerViewShowsCorrectCount() {
        val match1 = MyMatch(NAME_1, UID_1, IS_EXPANDED)
        val match2 = MyMatch(NAME_2, UID_2, IS_EXPANDED)
        val context = mock(Context::class.java)
        val list: ArrayList<MyMatch> = arrayListOf()
        val listener = mock(MyMatchesAdapter.OnItemClickListener::class.java)
        list.addAll(listOf(match1, match2))
        val viewHolder: RecyclerView.Adapter<MyMatchesAdapter.ViewHolder> =
            MyMatchesAdapter(list, arrayListOf(), context, listener)
        MatcherAssert.assertThat(viewHolder.itemCount, IsEqual.equalTo(2))
    }

    @Test
    fun itemTypeIsCorrect() {
        val match1 = MyMatch(NAME_1, UID_1, IS_EXPANDED)
        val match2 = MyMatch(NAME_2, UID_2, IS_EXPANDED)
        val context = mock(Context::class.java)
        val list: ArrayList<MyMatch> = arrayListOf()
        val listener = mock(MyMatchesAdapter.OnItemClickListener::class.java)
        list.addAll(listOf(match1, match2))
        val viewHolder: RecyclerView.Adapter<MyMatchesAdapter.ViewHolder> =
            MyMatchesAdapter(list, arrayListOf(), context, listener)
        val defaultType = 0
        MatcherAssert.assertThat(viewHolder.getItemViewType(0), IsEqual.equalTo(defaultType))
    }
}