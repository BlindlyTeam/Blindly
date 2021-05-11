package ch.epfl.sdp.blindly.main_screen.match.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R

/**
 * An adapter to display the cards in the match activity as a stack
 *
 * @property profiles the profiles to show
 */
class CardStackAdapter(
    private var profiles: List<Profile> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    /**
     * Called when the RecyclerView needs a new ViewHolder of the given type to represent an item
     *
     * @param parent the view into which the ViewHolder will be attached
     * @param viewType the view type of the new View
     * @return a ViewHolder that holds a View of the given type
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_profile, parent, false))
    }

    /**
     * Called by RecyclerView to display the data at the specified position
     *
     * @param holder a holder containing the different views
     * @param position the position in the stack
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]
        val v = holder.itemView.findViewById(R.id.item_image) as ImageView
        v.setImageResource(R.mipmap.blindly_launcher_foreground)
        holder.name.text = profile.name
        holder.age.text = profile.age.toString()
    }

    /**
     * Get the number of profiles in the card stack
     *
     * @return the number of profiles
     */
    override fun getItemCount(): Int {
        return profiles.size
    }

    /**
     * Class that contains the views of the different attributes of the profiles
     *
     * @param view containing the attributes
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.item_name)
        var age: TextView = view.findViewById(R.id.item_age)
    }
}
