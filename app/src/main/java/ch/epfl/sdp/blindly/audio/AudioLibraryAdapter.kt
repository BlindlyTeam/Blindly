package ch.epfl.sdp.blindly.audio

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.animations.RecordAnimations
import ch.epfl.sdp.blindly.profile_setup.EXTRA_USER
import ch.epfl.sdp.blindly.profile_setup.ProfileFinished
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.serialization.json.Json
import java.io.File

const val PRESENTATION_AUDIO_NAME = "PresentationAudio.amr"

/**
 * Adapter to use a RecyclerView as an audio library.
 *
 * @property recordList list of AudioRecords currently in the adapter
 * @property viewHolderList list of ViewHolders corresponding to each record
 * @property context context of the RecyclerView
 * @property listener handles clicks on items
 */
class AudioLibraryAdapter(
    var recordList: ArrayList<AudioRecord>,
    private var viewHolderList: ArrayList<ViewHolder>,
    var context: Context,
    private val listener: OnItemClickListener,
    private var userBuilder: User.Builder?,
    private val userHelper: UserHelper,
    private val recordings: FirebaseRecordings,
    private val activity: RecordingActivity,
) : RecyclerView.Adapter<AudioLibraryAdapter.ViewHolder>() {
    var blindlyMediaPlayer = BlindlyMediaPlayer()
    private lateinit var recordingPath: String

    /**
     * Custom ViewHolder class that contains all the elements that will be used later on in
     * [onBindViewHolder].
     *
     * @constructor
     * Sets the click listener for the layout that handles the layout expand click (the one with
     * the name and duration), and sets the visibility of the expandable layout to gone.
     *
     * @param view the current view
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val recordName: TextView = view.findViewById(R.id.recordName)
        val recordDuration: TextView = view.findViewById(R.id.recordDuration)
        val nameDurationLayout: LinearLayout = view.findViewById(R.id.nameDurationLayout)
        val expandableLayout: RelativeLayout = view.findViewById(R.id.audioPlayLayout)
        val playPauseButton: FloatingActionButton = view.findViewById(R.id.playPauseButton)
        val playBar: SeekBar = view.findViewById(R.id.playBar)
        val playTimer: Chronometer = view.findViewById(R.id.audioTimer)
        val remainingTimer: Chronometer = view.findViewById(R.id.remainingTimer)
        val selectButton: Button = view.findViewById(R.id.selectButton)

        init {
            nameDurationLayout.setOnClickListener(this)
            expandableLayout.visibility = View.GONE
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    /**
     * Collapses all record layouts in the view.
     */
    fun collapseLayouts() {
        for (i in 0 until recordList.size) {
            if (recordList[i].isExpanded) {
                val viewHolder = viewHolderList[i]
                blindlyMediaPlayer.setStoppedView(
                    viewHolder.playTimer, viewHolder.remainingTimer,
                    viewHolder.playPauseButton, false
                )
                RecordAnimations.collapse(viewHolder.expandableLayout)
                recordList[i].isExpanded = false
            }
        }
    }

    /**
     * Creates new views for each item of the list.
     * This function is invoked by the layout manager.
     *
     * @param viewGroup the group where the ViewHolder belongs
     * @param viewType the type of view
     * @return a new view, which defines the UI of the list items
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.audio_recording, null)
        viewHolderList.add(ViewHolder(view))
        return ViewHolder(view)
    }

    /**
     * Binds all the content to the view, and handles click listeners.
     * This function is invoked by the layout manager.
     *
     * @param viewHolder the ViewHolder of the current item
     * @param position the position of the item in the RecyclerView
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val playBar = viewHolder.playBar
        val playTimer = viewHolder.playTimer
        val remainingTimer = viewHolder.remainingTimer
        val playPauseButton = viewHolder.playPauseButton
        val movePlayBarThread = blindlyMediaPlayer.createPlayBarThread(playBar)

        blindlyMediaPlayer.bindSeekBarNavigation(
            playBar, playTimer, remainingTimer, playPauseButton,
            movePlayBarThread, recordList[position].filePath
        )

        viewHolder.recordName.text = recordList[position].name
        viewHolder.recordDuration.text = recordList[position].durationText

        /*
         * When the layout containing the name and duration is clicked, the layout expands or
         * collapses, and the layout is reset so that it is brand new when re-opened.
         */
        viewHolder.nameDurationLayout.setOnClickListener {
            val notIsExpanded = !recordList[position].isExpanded
            toggleLayout(notIsExpanded, viewHolder.expandableLayout)
            recordList[position].isExpanded = notIsExpanded
            blindlyMediaPlayer.resetRecordPlayer(
                recordList[position].filePath,
                playTimer,
                remainingTimer,
                playPauseButton,
                playBar
            )
        }

        blindlyMediaPlayer.setCountDownTimer(remainingTimer)

        /*
         * Handles clicks of the play/pause button, according to the current state of the media
         * player.
         */
        viewHolder.playPauseButton.setOnClickListener {
            blindlyMediaPlayer.handlePlayBarClick(
                recordList[position].filePath,
                playTimer,
                remainingTimer,
                playPauseButton,
                playBar,
                movePlayBarThread
            )
        }

        /*
         * If the select button is clicked, the selected file is saved and the userHelper is sent to the
         * profile finished activity.
         */
        viewHolder.selectButton.setOnClickListener {
            blindlyMediaPlayer.mediaPlayer?.release()
            saveRecording(position)
        }
    }

    override fun getItemCount() = recordList.size

    /**
     * Expands or collapses the layout of the clicked audio file, and collapses other ones by
     * calling [collapseLayouts].
     *
     * @param isExpanded if the record is currently expanded in layout
     * @param layoutExpand the layout to expand/collapse
     */
    private fun toggleLayout(isExpanded: Boolean, layoutExpand: RelativeLayout) {
        collapseLayouts()
        if (isExpanded) {
            RecordAnimations.expand(layoutExpand)
        } else {
            RecordAnimations.collapse(layoutExpand)
        }
    }

    /**
     * Saves the recording at a given position in the list. It is saved in the app's directory
     * as well as in Firebase storage, and can be easily retrieved.
     *
     * @param position the position of the file we want to save
     */
    private fun saveRecording(position: Int) {
        val filePath = recordList[position].filePath
        val currentRecording = File(filePath)
        val newFile = File("${context.filesDir.absolutePath}/$PRESENTATION_AUDIO_NAME")
        currentRecording.copyTo(
            newFile,
            overwrite = true
        )
        val userId = userHelper.getUserId()
        recordingPath = "Recordings/$userId-$PRESENTATION_AUDIO_NAME"
        userBuilder?.setRecordingPath(recordingPath)
        recordings.putFile(recordingPath, newFile, object :
            FirebaseRecordings.RecordingOperationCallback() {
            override fun onSuccess() {
                if (userBuilder != null)
                    startProfileFinished()
                else
                    startProfilePage()
            }

            override fun onError() {
                Toast.makeText(
                    context,
                    context.getString(R.string.upload_record_failed),
                    Toast.LENGTH_LONG
                )
                    .show()
                if (userBuilder != null)
                    startProfileFinished()
                else
                    startProfilePage()
            }
        })
    }

    private fun startProfileFinished() {
        val bundle = Bundle()
        if (userBuilder != null) {
            bundle.putSerializable(
                EXTRA_USER,
                Json.encodeToString(User.Builder.serializer(), userBuilder!!)
            )
        }
        val intent = Intent(context, ProfileFinished::class.java)
        intent.putExtras(bundle)
        startActivity(context, intent, null)
    }

    private fun startProfilePage() {
        activity.onBackPressed()
    }

    /**
     * Handles clicks on different items
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}