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
import view.Dashboard
import view.ImportTrack
import view.ImportTrackSpotify
import view.ProfilePage

class ImportTrackSpotifyViewModel():ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: ImportTrackSpotify) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun backToDash(){
        val i = Intent(ctx, Dashboard::class.java)
        ctx.startActivity(i)
    }

    fun importSpotify(url :String){
        try {
            val retrofitBuilder = retrofitClient.createAPIRequestWithToken(constants.bearerToken)
            val request = RequestDataInterface.ImportFromSpotifyRequest(url)
            val retrofitData = retrofitBuilder.importTrackFromSpotify(request)
            retrofitData.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.e("spoti response:", "retrieving body")
                    if (response.isSuccessful) {
                        var responseBody = response.body()
                        Log.e("add Track response: ", responseBody.toString() ?: "Response body is null")
                        Toast.makeText(ctx, "Track Imported Successfully !", Toast.LENGTH_LONG).show()
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("import error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Log.e("spoti error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.e("spoti error: ", t.toString())
                }
            })

        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }

    }
}