package viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface
import network.constants
import view.DetailedTrack

class SearchTrackRecViewAdapter(private val context : Context, private val data : List<RequestDataInterface.SearchTrackResponse>)
    : RecyclerView.Adapter<SearchTrackRecViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackRecViewAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_track_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchTrackRecViewAdapter.ViewHolder, position: Int) {
        val track = data[position]
        Log.e("on bind", track.toString())
        Log.e("on bind data", data.toString())
        // Populate the UI elements with track data
        //holder.trackName.text = data[1].toString()
        //holder.trackArtist.text = data[3].toString() // Assuming trackArtist is a property of RequestDataInterface.SearchTrackResponse

        val artistNames = track.artists.map { it.name } // Extract artist names
        val artistNamesString = artistNames.joinToString(", ") // Join artist names into a single string
        holder.trackName.text = track.name
        holder.trackArtist.text = artistNamesString
        Log.e("artiz: ", track.artists.toString())
        Log.e("names: ", artistNames.toString())
        Log.e("name string: ", artistNamesString)
        holder.item.setOnClickListener {
            val intent = Intent(holder.item.context, DetailedTrack::class.java)
            intent.putExtra("id", track.id)
            intent.putExtra("from", "searchTrack")
            constants.currentPlaylistID = "0"
            holder.item.context.startActivity(intent)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackName: TextView = itemView.findViewById(R.id.trackNameTxt)
        val trackArtist : TextView = itemView.findViewById(R.id.trackArtist)
        val item : RelativeLayout = itemView.findViewById(R.id.trackRow)

    }

}