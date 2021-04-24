package ch.epfl.sdp.blindly.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


private const val CURRENT_USER_SENDING = 0
private const val REMOTE_USER_SENDING = 1
val currentFirebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser

class ChatAdapter(private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.textView4)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        var view: View? = null
        view = if (viewType == CURRENT_USER_SENDING) {
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.message_outgoing, viewGroup, false)
        } else {//remote user sending
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.message_incoming, viewGroup, false)
        }
        return view?.let { ViewHolder(it) }!!
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = messageList[position].messageText
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = messageList.size

    override fun getItemViewType(position: Int): Int {
        if (messageList[position].currentUserId == currentFirebaseUser.uid) {
            //if (messageList[position].isCurrentUser) {
            return CURRENT_USER_SENDING
        }
        return REMOTE_USER_SENDING
    }


}
