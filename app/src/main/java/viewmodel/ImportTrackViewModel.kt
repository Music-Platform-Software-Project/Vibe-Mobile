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

    fun addTrack(trackData : RequestDataInterface.addTrackRequest){
        try {

            val retrofitBuilder = retrofitClient.createAPIRequestWithToken(constants.bearerToken)
            val token = "Bearer " + constants.bearerToken
            Log.e("bearer set username", constants.bearerToken)
            val retrofitData = retrofitBuilder.addTrack(trackData)
            retrofitData.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.e("login response:", "retrieving body")
                    if (response.isSuccessful) {
                        var responseBody = response.body()
                        Log.e("add Track response: ", responseBody.toString() ?: "Response body is null")
                        Toast.makeText(ctx, "Track ${trackData.track.name} imported successfully !", Toast.LENGTH_LONG).show()
                        ctx.startActivity(Intent(ctx, Dashboard::class.java ))
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

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
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