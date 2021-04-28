package ch.epfl.sdp.blindly.chat

import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotSame
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatAdapterTest {

    @Test
    fun recyclerViewShowsCorrectCount() {
        val m1 = Message("mes1", "2938473dsgfd298")
        val m2 = Message("mes2", "293s73dshfd298")
        val list: ArrayList<Message> = arrayListOf<Message>()
        list.addAll(listOf(m1, m2))
        val abc: RecyclerView.Adapter<ChatAdapter.ViewHolder> = ChatAdapter(list)
        abc.notifyDataSetChanged()
        assertEquals(2, abc.itemCount)
    }

    @Test
    fun itemTypeIsCorrect() {
        val m1 = Message("mes1", "2938473dsgfd298")
        val m2 = Message("mes2", "293s73dshfd298")
        val list: ArrayList<Message> = arrayListOf<Message>()
        list.addAll(listOf(m1, m2))
        val abc: RecyclerView.Adapter<ChatAdapter.ViewHolder> = ChatAdapter(list)
        assertEquals(1, abc.getItemViewType(1))
    }


    @Test
    fun messageObjectIsCorrect() {
        val userId1 = "23094hsdkjfh"
        val userId2 = "23094823049"
        val m1 = Message("a message", userId1)
        val m2 = Message("some message", userId2)
        assertNotSame(m1.timestamp!!, m2.timestamp!!)
        assertEquals("some message", m2.messageText)
        assertEquals(userId2, m2.currentUserId)
    }


}
