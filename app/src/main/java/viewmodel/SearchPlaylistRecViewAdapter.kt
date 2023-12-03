package viewmodel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface

class SearchPlaylistRecViewAdapter(private val context : Context, private val data : List<RequestDataInterface.SearchPlaylistResponse>)
    : RecyclerView.Adapter<SearchPlaylistRecViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPlaylistRecViewAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_playlist_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchPlaylistRecViewAdapter.ViewHolder, position: Int) {
        val playlist = data[position]
        Log.e("on bind", playlist.toString())
        Log.e("on bind data", data.toString())
        // Populate the UI elements with track data
        holder.playlistName.text = playlist.name

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistName: TextView = itemView.findViewById(R.id.playlistNameTxt)

    }
}