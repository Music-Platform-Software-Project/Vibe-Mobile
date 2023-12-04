package viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
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
import view.PlaylistDetailRecViewAdapter

class DetailedPlaylistViewModel(): ViewModel() {


    private lateinit var ctx: DetailedPlaylist


    fun setContext(ctx: DetailedPlaylist) {
        this.ctx = ctx
    }

    fun getDetails(id: String){
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)
            val token = "Bearer " + constants.bearerToken
            val request = RequestDataInterface.acceptRequestPayload(id)
            val retrofitData = retrofitBuilder.getPlaylistDetails(token, request)
            Log.e("playlist process", "going...")

            retrofitData.enqueue(object : Callback<RequestDataInterface.PlaylistDetailResponse> {

                override fun onResponse(call: Call<RequestDataInterface.PlaylistDetailResponse>, response: Response<RequestDataInterface.PlaylistDetailResponse>) {
                    Log.e("playlist response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("playlist response: ",
                            (responseBody ?: "Response body is null").toString()
                        )
                        (ctx as? Activity)?.findViewById<TextView>(R.id.playlistName)?.text = responseBody?.name.toString()
                        (ctx as? Activity)?.findViewById<TextView>(R.id.numOfTracks)?.text =
                            "Number of Tracks: ${responseBody?.trackCount.toString()}"
                        val time = secondsToMinutes(responseBody?.totalDuration!!)
                        (ctx as? Activity)?.findViewById<TextView>(R.id.totalDuration)?.text =
                            "Total Duration: $time"
                        constants.currentPlaylistID = responseBody.id
                        val trackList = responseBody?.tracks
                        val recView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.tracksInPlaylist)
                        recView?.layoutManager = LinearLayoutManager(ctx)
                        val adapter = PlaylistDetailRecViewAdapter(trackList!!)
                        recView?.adapter = adapter
                        recView?.layoutManager = LinearLayoutManager(ctx)


                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("playlist error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Log.e("playlist error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<RequestDataInterface.PlaylistDetailResponse>, t: Throwable) {
                    Log.e("playlist error: ", t.toString())
                }
            })

        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }

    private fun secondsToMinutes(duration: Int): String {
        val minutes = (duration / 60).toInt().toString()
        val seconds = (duration % 60).toString()
        return "$minutes:$seconds"
    }
}