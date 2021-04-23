package ch.epfl.sdp.blindly.match

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R

class CardStackAdapter(
    private var profiles: List<Profile> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_profile, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]
        val v = holder.itemView.findViewById(R.id.item_image) as ImageView
        v.setImageResource(R.mipmap.blindly_launcher_foreground)
        holder.name.text = profile.name
        holder.age.text = profile.age.toString()
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.item_name)
        var age: TextView = view.findViewById(R.id.item_age)
    }
}