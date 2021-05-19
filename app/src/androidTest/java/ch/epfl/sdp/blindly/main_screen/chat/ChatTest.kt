package ch.epfl.sdp.blindly.main_screen.chat

import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.main_screen.my_matches.chat.ChatAdapter
import ch.epfl.sdp.blindly.main_screen.my_matches.chat.Message
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNot
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatTest {

    @Test
    fun recyclerViewShowsCorrectCount() {
        val m1 = Message("mes1", USER_ID_1)
        val m2 = Message("mes2", USER_ID_2)
        val list: ArrayList<Message<String>> = arrayListOf()
        list.addAll(listOf(m1, m2))
        val abc: RecyclerView.Adapter<ChatAdapter.ViewHolder> = ChatAdapter(USER_ID_1, list)
        MatcherAssert.assertThat(abc.itemCount, IsEqual.equalTo(2))
    }

    @Test
    fun itemTypeIsCorrect() {
        val m1 = Message("mes1", USER_ID_1)
        val m2 = Message("mes2", USER_ID_2)
        val list: ArrayList<Message<String>> = arrayListOf()
        list.addAll(listOf(m1, m2))
        val abc: RecyclerView.Adapter<ChatAdapter.ViewHolder> = ChatAdapter(USER_ID_1, list)
        val remoteUserSending = 1
        MatcherAssert.assertThat(abc.getItemViewType(1), IsEqual.equalTo(remoteUserSending))
    }

    @Test
    fun messageObjectIsCorrect() {
        val userId = "23094823049"
        val m1 = Message("some message", userId)
        MatcherAssert.assertThat(m1.messageText, IsEqual.equalTo("some message"))
        MatcherAssert.assertThat(m1.currentUserId, IsEqual.equalTo(userId))
    }

    @Test
    fun timestampIsUnique() {
        val userId1 = "23094hsdkjfh"
        val m1 = Message("a message", userId1)
        Thread.sleep(1000)
        val userId2 = "23094823049"
        val m2 = Message("some message", userId2)
        MatcherAssert.assertThat(m1.timestamp!!, IsNot(equalTo(m2.timestamp!!)))
    }

    companion object {
        private const val USER_ID_1 = "2938473dsgfd298"
        private const val USER_ID_2 = "293s73dshfd298"
    }
}
