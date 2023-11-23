package viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import model.RequestDataInterface
import network.RetrofitClient
import network.constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import view.ImportTrack
import view.PersonalizedTracks
import view.ProfilePage

class ImportTrackViewModel() : ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: ImportTrack) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun backToProfile(){
        val i = Intent(ctx, ProfilePage::class.java)
        ctx.startActivity(i)
    }

    fun addTrack(name : String, artist : List<String>, album :String,
                 genre : String, tempo : Int,  acousticness: Int, energy: Int,
                  instrumentalness: Int, mood: String){
        try {

            val retrofitBuilder = retrofitClient.createAPIRequest()
            val token = "Bearer " + constants.bearerToken
            Log.e("bearer set username", constants.bearerToken)
            val data = RequestDataInterface.TrackData(name, artist, album, genre, tempo, acousticness, energy, instrumentalness, mood)
            val request = RequestDataInterface.addTrackRequest(data)
            val retrofitData = retrofitBuilder.addTrack(token,request)
            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("login response:", "retrieving body")
                    if (response.isSuccessful) {
                        var responseBody = response.body()
                        responseBody =  responseBody!!.substringAfter(":").trim()
                        responseBody = responseBody.replace(Regex("[\"{}]"), "").trim()
                        Toast.makeText(ctx, responseBody, Toast.LENGTH_LONG).show()
                        //constants.bearerToken = responseBody.toString()
                        //Log.e("token tag", "Bearer Token is: ${constants.bearerToken}")
                        Log.e("add Track response: ", responseBody ?: "Response body is null")
                        // Parse the responseBody as needed or handle the string response
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("import error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Log.e("login error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("login error: ", t.toString())
                }
            })

        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }

}