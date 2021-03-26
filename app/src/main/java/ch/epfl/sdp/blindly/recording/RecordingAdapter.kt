package ch.epfl.sdp.blindly.recording

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R

/**
 * Serves as an adapter to add audio recordings in a RecyclerView
 */
class RecordingAdapter(var recordings: List<AudioRecord>,
                       var context: Context,
                       private val listener: OnItemClickListener)
    : RecyclerView.Adapter<RecordingAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    var currentSelectionPos = -1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var recordName: TextView = view.findViewById(R.id.recordName)
        val recordDuration: TextView = view.findViewById(R.id.recordDuration)
        val selectButton: Button = view.findViewById(R.id.selectButton)
        private val nameDurationLayout: LinearLayout = view.findViewById(R.id.nameDurationLayout)

        init {
            // Define click listener for the ViewHolder's View.
            nameDurationLayout.setOnClickListener(this)
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
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(context).inflate(R.layout.audio_recording, null)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.recordName.text = recordings[position].name
        viewHolder.recordDuration.text = recordings.get
        // Show / Hide selection
        if (position == currentSelectionPos) {

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = recordings.size

    /**
     * Used to handle clicks in the activity
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}