package ch.epfl.sdp.blindly.chat

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue


class ChatActivity : AppCompatActivity() {

    private lateinit var currentUserId: String
    private lateinit var matchId: String
    private lateinit var chatId: String

    private var chatMessages: ArrayList<Message>? = arrayListOf()
    var mChatLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        currentUserId = FirebaseAuth.getInstance().currentUser.uid
        val bundle = this.intent.extras
        if (bundle != null) {
            matchId == bundle.getString("matchId")
        } else {
            //for test
            matchId = "LQ9JDQQQakWJeHUwWzc2Dt5tBEC3"
            //matchId="kh5EpYDCqXNtKWKTUYA02Kp65NB3"
        }

        //this is done to get the same chatId from both sides
        chatId = if (currentUserId < matchId) {
            "($currentUserId, $matchId)"
        } else {
            "($matchId, $currentUserId)"
        }

        findViewById<RecyclerView>(R.id.recyclerView).layoutManager = mChatLayoutManager
        findViewById<RecyclerView>(R.id.recyclerView).adapter =
            getMessages()?.let { ChatAdapter(it) }
        receiveMessages()
    }


    /**
     * Triggered once the send button is pressed
     *
     * @param view the current view
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
            currentUserId
        )

        FirebaseDatabase.getInstance("https://blindly-24119-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("messages").child(chatId)
            .child(newMessage.timestamp.toString()).setValue(newMessage)

        //clear the text after sending the message
        findViewById<EditText>(R.id.newMessageText).text.clear()

    }

    private fun receiveMessages() {
        val myRef =
            FirebaseDatabase.getInstance("https://blindly-24119-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("messages").child(chatId)

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