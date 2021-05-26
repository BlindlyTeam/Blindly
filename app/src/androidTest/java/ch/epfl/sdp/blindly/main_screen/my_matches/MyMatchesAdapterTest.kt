package ch.epfl.sdp.blindly.main_screen.my_matches

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.user.UserHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import javax.inject.Inject

@HiltAndroidTest
class MyMatchesAdapterTest {

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var userRepository: UserRepository

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun afterEach() {
        Intents.release()
    }

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
                listener,
                userHelper,
                userRepository
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
                listener,
                userHelper,
                userRepository
            )
        val defaultType = 0
        MatcherAssert.assertThat(viewHolder.getItemViewType(0), IsEqual.equalTo(defaultType))
    }

    @Test
    fun setupAdapterForRecyclerView() {
        val match1 = MyMatch(NAME_1, UID_1, IS_EXPANDED, true)
        val match2 = MyMatch(NAME_2, UID_2, IS_EXPANDED, false)
        val list: ArrayList<MyMatch> = arrayListOf()
        val listener = mock(MyMatchesAdapter.OnItemClickListener::class.java)
        list.addAll(listOf(match1, match2))
        val rv = RecyclerView(ApplicationProvider.getApplicationContext())
        rv.layoutManager = LinearLayoutManager(ApplicationProvider.getApplicationContext())
        val adapter = MyMatchesAdapter(
            list,
            arrayListOf(),
            ApplicationProvider.getApplicationContext(),
            listener,
            userHelper,
            userRepository
        )
        rv.adapter = adapter
        adapter.my_matches.add(MyMatch(NAME_1, UID_2, IS_EXPANDED, IS_DELETED))
        adapter.notifyDataSetChanged()
    }
}