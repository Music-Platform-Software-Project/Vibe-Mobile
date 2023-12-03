package viewmodel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface

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
        holder.trackName.text = track.name
        holder.trackArtist.text = track.artists.joinToString(", ")

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackName: TextView = itemView.findViewById(R.id.trackNameTxt)
        val trackArtist : TextView = itemView.findViewById(R.id.trackArtist)

    }

}