package ch.epfl.sdp.blindly

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Serves as an adapter to add audio recordings in a RecyclerView
 */
class RecordingAdapter(var recordingsNames: ArrayList<String>, private val listener: OnItemClickListener): RecyclerView.Adapter<RecordingAdapter.ViewHolder>(){
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val recording: TextView = view.findViewById(R.id.recording)

        init {
            // Define click listener for the ViewHolder's View.
            recording.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            // Forward the click to the main activity
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.audio_recording, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.recording.text = recordingsNames[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = recordingsNames.size

    /**
     * Used to handle clicks in the activity
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}