package ch.epfl.sdp.blindly.main_screen.my_matches

import android.content.Context
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.chat.ChatAdapter
import ch.epfl.sdp.blindly.main_screen.chat.ChatTest
import ch.epfl.sdp.blindly.main_screen.chat.Message
import ch.epfl.sdp.blindly.main_screen.match.my_matches.MyMatch
import ch.epfl.sdp.blindly.main_screen.match.my_matches.MyMatchesAdapter
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.Test
import org.junit.runner.manipulation.Ordering
import org.mockito.Mockito.mock

@HiltAndroidTest
class MyMatchesAdapterTest {
   companion object{
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
            MyMatchesAdapter(list, arrayListOf(), context , listener)
        MatcherAssert.assertThat(viewHolder.itemCount, IsEqual.equalTo(2))
    }

    @Test
    fun createAdapterAndAttachToRecyclerView() {
        val match1 = MyMatch(NAME_1, UID_1, IS_EXPANDED)
        val match2 = MyMatch(NAME_2, UID_2, IS_EXPANDED)
        val context = mock(Context::class.java)
        val list: ArrayList<MyMatch> = arrayListOf()
        val listener = mock(MyMatchesAdapter.OnItemClickListener::class.java)
        list.addAll(listOf(match1, match2))
        val recyclerView =  mock(RecyclerView::class.java)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = MyMatchesAdapter(list, arrayListOf(), context , listener)
        recyclerView.adapter = adapter
    }

}