package viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
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
import view.my_room

class AddRoomTrackRecViewAdapter(private val context : Context, private val data : List<RequestDataInterface.SearchTrackResponse>)
    : RecyclerView.Adapter<AddRoomTrackRecViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddRoomTrackRecViewAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_track_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AddRoomTrackRecViewAdapter.ViewHolder, position: Int) {
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
            addRoomTrack(track.id)
            val intent = Intent(holder.item.context, my_room::class.java)
            intent.putExtra("trackName", track.name)
            intent.putExtra("keyword", "addRoomTrack")
            holder.item.context.startActivity(intent)
        }

    }
    private fun addRoomTrack(trackId : String){
        try {

            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)

            val token = "Bearer " + constants.bearerToken
            val request = RequestDataInterface.SetRoomTrackRequest(trackId)
            val retrofitData = retrofitBuilder.setRoomTrack(token, request)
            Log.e("adding roomtrack", "going...")

            retrofitData.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.e("adding roomtrack:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("adding roomtrack",
                            (responseBody ?: "Response body is null").toString()
                        )
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("adding roomtrack error ", "HTTP ${response.code()}: $errorBody")

                        } catch (e: Exception) {
                            Log.e("adding roomtrack error", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.e("adding roomtrack error ", t.toString())
                }
            })
        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackName: TextView = itemView.findViewById(R.id.trackNameTxt)
        val trackArtist : TextView = itemView.findViewById(R.id.trackArtist)
        val item : RelativeLayout = itemView.findViewById(R.id.trackRow)

    }
}