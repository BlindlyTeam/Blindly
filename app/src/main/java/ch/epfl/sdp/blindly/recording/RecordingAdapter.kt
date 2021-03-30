package ch.epfl.sdp.blindly.recording

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.animations.RecordAnimations

/**
 * Serves as an adapter to add audio recordings in a RecyclerView
 */
class RecordingAdapter(var recordList: ArrayList<AudioRecord>,
                       var context: Context,
                       private val listener: OnItemClickListener)
    : RecyclerView.Adapter<RecordingAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    var currentSelectionPos = -1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val recordName: TextView = view.findViewById(R.id.recordName)
        val recordDuration: TextView = view.findViewById(R.id.recordDuration)
        val nameDurationLayout: LinearLayout = view.findViewById(R.id.nameDurationLayout)
        val expandableLayout: RelativeLayout = view.findViewById(R.id.audioPlayLayout)

        init {
            // Define click listener for the ViewHolder's View.
            nameDurationLayout.setOnClickListener(this)
            expandableLayout.visibility = View.GONE
        }

        override fun onClick(v: View?) {
            // Forward the click to the main activity
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    private fun toggleLayout(isExpanded: Boolean, v: View, layoutExpand: RelativeLayout): Boolean {
        if (isExpanded) {
            RecordAnimations.expand(layoutExpand)
        } else {
            RecordAnimations.collapse(layoutExpand)
        }
        return isExpanded
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(context).inflate(R.layout.audio_recording, null)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.recordName.text = recordList[position].name
        viewHolder.recordDuration.text = recordList[position].durationText
        viewHolder.nameDurationLayout.setOnClickListener {
            val show: Boolean = toggleLayout(!recordList[position].isExpanded, it, viewHolder.expandableLayout)
            recordList[position].isExpanded = show
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = recordList.size

    /**
     * Used to handle clicks in the activity
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}