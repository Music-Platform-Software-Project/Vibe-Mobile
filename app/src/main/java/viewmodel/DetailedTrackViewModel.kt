package viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface
import network.APIRequest
import network.RetrofitClient
import network.constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import view.Dashboard
import view.DetailedTrack
import view.PlaylistDetailRecViewAdapter

class DetailedTrackViewModel(): ViewModel() {

    private lateinit var ctx: Context


    fun setContext(ctx: DetailedTrack) {
        this.ctx = ctx
    }

    fun getAll(id: String) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)

            val token = "Bearer " + constants.bearerToken
            val request = RequestDataInterface.TrackDetailRequest(id)
            val retrofitData = retrofitBuilder.getTrackDetails(token, request)
            Log.e("getAll process", "going...")

            retrofitData.enqueue(object : Callback<RequestDataInterface.TrackDetailResponse> {
                override fun onResponse(call: Call<RequestDataInterface.TrackDetailResponse>, response: Response<RequestDataInterface.TrackDetailResponse>) {
                    Log.e("getAll response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("getAll response: ",
                            (responseBody ?: "Response body is null").toString()
                        )
                        (ctx as? Activity)?.findViewById<TextView>(R.id.trackName)?.text = responseBody?.name
                        (ctx as? Activity)?.findViewById<TextView>(R.id.trackArtist)?.text = manageArtist(responseBody?.artists)
                        (ctx as? Activity)?.findViewById<TextView>(R.id.trackTempo)?.text = manageTempo(responseBody?.tempo)
                        (ctx as? Activity)?.findViewById<TextView>(R.id.trackAcoustic)?.text = manageAcoustic(responseBody?.acousticness)
                        (ctx as? Activity)?.findViewById<TextView>(R.id.trackInstrument)?.text = manageInstrument(responseBody?.instrumentalness)
                        (ctx as? Activity)?.findViewById<TextView>(R.id.trackEnergy)?.text = manageEnergy(responseBody?.energy)
                        val mood = responseBody?.mood
                        (ctx as? Activity)?.findViewById<TextView>(R.id.trackMood)?.text = "Mood: $mood"

                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("getAll error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Log.e("getAll error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<RequestDataInterface.TrackDetailResponse>, t: Throwable) {
                    Log.e("getAll error: ", t.toString())
                }
            })

        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }



    private fun manageEnergy(energy: Int?): CharSequence? {
        var str = ""
        if(energy!!<31){
            str = "Energy: Low"
        }
        else if(energy!! >= 31 && energy < 62){
            str = "Energy: Medium"
        }
        else{
            str = "Energy: High"
        }
        return str
    }

    private fun manageInstrument(instrumentalness: Int?): CharSequence? {
        var str = ""
        if(instrumentalness!!<31){
            str = "Instrumentalness: Vocal"
        }
        else if(instrumentalness!! >= 31 && instrumentalness < 62){
            str = "Instrumentalness: Mix"
        }
        else{
            str = "Instrumentalness: No Vocal"
        }
        return str
    }

    private fun manageAcoustic(acousticness: Int?): CharSequence? {
        var str = ""
        if(acousticness!!<31){
            str = "Acousticness: Digital"
        }
        else if(acousticness!! >= 31 && acousticness < 62){
            str = "Acousticness: Mix"
        }
        else{
            str = "Acousticness: Analog"
        }
        return str
    }

    private fun manageTempo(tempo: Int?): CharSequence? {

        val tempo = tempo.toString()
        return "Tempo: $tempo BPM"
    }

    private fun manageArtist(artists: List<String>?): CharSequence? {

        var str = ""
        if(artists != null){
            for (artist in artists){
                str = "$str $artist"
            }
        }
        return str
    }

    fun deleteTrack(id: String) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)
            val token = "Bearer " + constants.bearerToken
            val request = RequestDataInterface.TrackDetailRequest(id)
            val retrofitData = retrofitBuilder.deleteTrack(token, request)
            Log.e("delete track1 process", "going...")

            retrofitData.enqueue(object : Callback<Boolean> {

                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.e("delete track1 response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("delet track1 response: ",
                            (responseBody ?: "Response body is null").toString()
                        )
                        Toast.makeText(ctx, "Track Deleted", Toast.LENGTH_SHORT).show()
                        ctx.startActivity(Intent(ctx, Dashboard::class.java))
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("delete track1 error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Log.e("delete track1 error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.e("delete track1 error2: ", t.toString())
                }
            })

        }
        catch (e: Exception) {
            Log.e("error1", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }

    fun deleteTrack2(id: String, playlistID: String) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)
            val token = "Bearer " + constants.bearerToken
            val request = RequestDataInterface.TrackDetailRequestPlaylist(playlistID, id)
            val retrofitData = retrofitBuilder.deleteTrackFromPlaylist(token, request)
            Log.e("delete track process", "going...")

            retrofitData.enqueue(object : Callback<Boolean> {

                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.e("delete track response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("delete track response: ",
                            (responseBody ?: "Response body is null").toString()
                        )
                        Toast.makeText(ctx, "Track Deleted", Toast.LENGTH_SHORT).show()
                        ctx.startActivity(Intent(ctx, Dashboard::class.java))
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("delete track error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Log.e("delete track error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.e("delete track error2: ", t.toString())
                }
            })

        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }
}