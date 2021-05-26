package ch.epfl.sdp.blindly.main_screen.my_matches

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatch
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatchesAdapter
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
        private const val IS_DELETED = false
        private const val NAME_2 = "user2"
        private const val UID_2 = "uid2"

    }

    @Test
    fun recyclerViewShowsCorrectCount() {
        val match1 = MyMatch(NAME_1, UID_1, IS_EXPANDED, IS_DELETED)
        val match2 = MyMatch(NAME_2, UID_2, IS_EXPANDED, IS_DELETED)
        val list: ArrayList<MyMatch> = arrayListOf()
        val listener = mock(MyMatchesAdapter.OnItemClickListener::class.java)
        list.addAll(listOf(match1, match2))
        val viewHolder: RecyclerView.Adapter<MyMatchesAdapter.ViewHolder> =
            MyMatchesAdapter(
                list,
                arrayListOf(),
                ApplicationProvider.getApplicationContext(),
                listener
            )

        MatcherAssert.assertThat(viewHolder.itemCount, IsEqual.equalTo(2))
    }

    @Test
    fun itemTypeIsCorrect() {
        val match1 = MyMatch(NAME_1, UID_1, IS_EXPANDED, IS_DELETED)
        val match2 = MyMatch(NAME_2, UID_2, IS_EXPANDED, IS_DELETED)
        val list: ArrayList<MyMatch> = arrayListOf()
        val listener = mock(MyMatchesAdapter.OnItemClickListener::class.java)
        list.addAll(listOf(match1, match2))
        val viewHolder: RecyclerView.Adapter<MyMatchesAdapter.ViewHolder> =
            MyMatchesAdapter(
                list,
                arrayListOf(),
                ApplicationProvider.getApplicationContext(),
                listener
            )
        val defaultType = 0
        MatcherAssert.assertThat(viewHolder.getItemViewType(0), IsEqual.equalTo(defaultType))
    }

    @Test
    fun setupAdapterForRecyclerView() {
        val match1 = MyMatch(NAME_1, UID_1, IS_EXPANDED, IS_DELETED)
        val match2 = MyMatch(NAME_2, UID_2, IS_EXPANDED, IS_DELETED)
        val list: ArrayList<MyMatch> = arrayListOf()
        val listener = mock(MyMatchesAdapter.OnItemClickListener::class.java)
        list.addAll(listOf(match1, match2))
        val rv = RecyclerView(ApplicationProvider.getApplicationContext())
        rv.layoutManager = LinearLayoutManager(ApplicationProvider.getApplicationContext())
        val adapter = MyMatchesAdapter(
            list,
            arrayListOf(),
            ApplicationProvider.getApplicationContext(),
            listener
        )
        rv.adapter = adapter
        adapter.my_matches.add(MyMatch(NAME_1, UID_2, IS_EXPANDED, IS_DELETED))
        adapter.notifyDataSetChanged()
    }

    @Test
    fun clickingOnAdpaterExpandName() {
        val match1 = MyMatch(NAME_1, UID_1, IS_EXPANDED, false)
        val match2 = MyMatch(NAME_2, UID_2, IS_EXPANDED, true)
        val list: ArrayList<MyMatch> = arrayListOf()
        val listener = mock(MyMatchesAdapter.OnItemClickListener::class.java)
        list.addAll(listOf(match1, match2))
        val rv = RecyclerView(ApplicationProvider.getApplicationContext())
        rv.layoutManager = LinearLayoutManager(ApplicationProvider.getApplicationContext())
        val adapter = MyMatchesAdapter(
            list,
            arrayListOf(),
            ApplicationProvider.getApplicationContext(),
            listener
        )
        rv.adapter = adapter
        adapter.my_matches.add(MyMatch(NAME_1, UID_2, IS_EXPANDED, IS_DELETED))
        adapter.notifyDataSetChanged()
    }
}