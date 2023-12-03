package view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface

class PlaylistDetailRecViewAdapter(private val itemList: List<RequestDataInterface.Track>) : RecyclerView.Adapter<PlaylistDetailRecViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.trackImage)
        val name: TextView = itemView.findViewById(R.id.trackName)
        val artists: TextView = itemView.findViewById(R.id.trackArtist)
        val duration: TextView = itemView.findViewById(R.id.trackDuration)
        val rating: TextView = itemView.findViewById(R.id.trackRating)
        val item : LinearLayout = itemView.findViewById(R.id.trackRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        var inflateLayout = R.layout.track_in_playlist

        val itemView = LayoutInflater.from(parent.context).inflate(inflateLayout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.imageView.setImageResource(R.drawable.vibe_logo_2)
        holder.name.text = currentItem.name
        var names : String = ""
        for(name in currentItem.artists){
            names = "$names $name "
        }
        holder.artists.text = names
        var duration : Int = currentItem.duration
        val durationInMinutes : String = secondsToMinutes(duration)
        holder.duration.text = durationInMinutes
        var rating = "★ ${currentItem.rating}/5"
        holder.rating.text = rating

        holder.item.setOnClickListener {

        }

    }

    private fun secondsToMinutes(duration: Int): String {
        val minutes = (duration / 60).toInt().toString()
        val seconds = (duration % 60).toString()
        return "$minutes:$seconds"
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


}
