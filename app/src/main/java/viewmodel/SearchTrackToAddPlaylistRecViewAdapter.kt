package viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface
import network.APIRequest
import network.constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import view.DetailedPlaylist
import view.DetailedTrack
import view.ui.main.IncomingRequests

class SearchTrackToAddPlaylistRecViewAdapter(private val context : Context, private val data : List<RequestDataInterface.SearchTrackResponse>)
    : RecyclerView.Adapter<SearchTrackToAddPlaylistRecViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackToAddPlaylistRecViewAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_track_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchTrackToAddPlaylistRecViewAdapter.ViewHolder, position: Int) {
        val track = data[position]
        // Populate the UI elements with track data
        //holder.trackName.text = data[1].toString()
        //holder.trackArtist.text = data[3].toString() // Assuming trackArtist is a property of RequestDataInterface.SearchTrackResponse
        holder.trackName.text = track.name
        holder.trackArtist.text = track.artists.joinToString(", ")
        holder.item.setOnClickListener {
            addSongToPlaylist(constants.currentPlaylistID, track.id)
            val intent = Intent(holder.item.context, DetailedPlaylist::class.java)
            intent.putExtra("id", constants.currentPlaylistID)
            holder.item.context.startActivity(intent)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackName: TextView = itemView.findViewById(R.id.trackNameTxt)
        val trackArtist : TextView = itemView.findViewById(R.id.trackArtist)
        val item : RelativeLayout = itemView.findViewById(R.id.trackRow)

    }

    private fun addSongToPlaylist(currentPlaylistID: String, trackId: String) {
        try {

            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)

            val token = "Bearer " + constants.bearerToken
            val request = RequestDataInterface.AddTrackToPlaylist(currentPlaylistID, trackId)
            val retrofitData = retrofitBuilder.addTrackToPlaylist(token, request)
            Log.e("adding playlist", "going...")

            retrofitData.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.e("adding playlist:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("adding playlist ",
                            (responseBody ?: "Response body is null").toString()
                        )
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("adding playlist error ", "HTTP ${response.code()}: $errorBody")

                        } catch (e: Exception) {
                            Log.e("adding playlist error2 ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.e("adding playlist error ", t.toString())
                }
            })
        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }

}