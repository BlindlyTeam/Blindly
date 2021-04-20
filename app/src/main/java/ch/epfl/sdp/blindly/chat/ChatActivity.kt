package ch.epfl.sdp.blindly.chat

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue


class ChatActivity : AppCompatActivity() {


    private var chatMessages: ArrayList<Message>? = arrayListOf()
    var mChatLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        findViewById<RecyclerView>(R.id.recyclerView).layoutManager = mChatLayoutManager
        findViewById<RecyclerView>(R.id.recyclerView).adapter = getMessages()?.let { ChatAdapter(it) }
        receiveMessage()
    }


    /**
     * Triggered once the send button is pressed
     *
     * @param view
     */
    fun sendButtonActivate(view: View) {
        findViewById<Button>(R.id.sendButton).setOnClickListener {
            if (findViewById<EditText>(R.id.newMessageText).text.toString().isNotEmpty()) {
                sendMessage()
            }
        }
    }

    /**
     * Sends the message to the Realtime Database
     *
     */
    private fun sendMessage() {
        val newMessage = Message(
            findViewById<EditText>(R.id.newMessageText).text.toString(),
            "user3", "user4"
        )

        FirebaseDatabase.getInstance("https://blindly-24119-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("messages").child("(user3, user4)")
            .child(newMessage.timestamp.toString()).setValue(newMessage)

        //clear the text after sending the message
        findViewById<EditText>(R.id.newMessageText).text.clear()

    }

    private fun receiveMessage() {
        val myRef =
            FirebaseDatabase.getInstance("https://blindly-24119-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("messages").child("(user3, user4)")

        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue<Message>()
                if (message != null) {
                    chatMessages?.add(message)
                    findViewById<RecyclerView>(R.id.recyclerView).adapter?.notifyDataSetChanged()
                    findViewById<RecyclerView>(R.id.recyclerView).scrollToPosition(chatMessages!!.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }


    private fun getMessages(): ArrayList<Message>? {
        return chatMessages
    }


}