package viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface
import network.RetrofitClient
import network.constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import view.Dashboard
import view.FriendsRoom
import view.ProfilePage
import view.Settings

class FriendsRoomViewModel() : ViewModel() {
    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: FriendsRoom) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun switchToDashboard(){
        val intent = Intent(ctx, Dashboard::class.java)
        ctx.startActivity(intent)
    }

    fun switchToProfile(){
        val intent = Intent(ctx, ProfilePage::class.java)
        ctx.startActivity(intent)
    }

    fun switchToSetting(){
        val intent = Intent(ctx, Settings::class.java)
        ctx.startActivity(intent)
    }


    fun getImage(userID: String?) {
        try {
            val retrofitBuilder = retrofitClient.createAPIRequestWithToken(constants.bearerToken)
            val request = RequestDataInterface.PlaylistAverageRequest(userID!!)
            val retrofitData = retrofitBuilder.getUserRoom(constants.bearerToken, request)
            val trackList: MutableList<RequestDataInterface.SearchTrackResponse> = mutableListOf()
            retrofitData.enqueue(object : Callback<RequestDataInterface.getUserDataResponse> {
                override fun onResponse(
                    call: Call<RequestDataInterface.getUserDataResponse>,
                    response: Response<RequestDataInterface.getUserDataResponse>
                ) {
                    Log.e("get room response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("get room response: ", (responseBody ?: "Response body is null").toString())

                        val base64Image = responseBody?.selectedRoom?.image.toString()
                        val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)

                        // Decode the byte array to a Bitmap
                        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                        // Set the Bitmap to the ImageView
                        val imageView = (ctx as? Activity)?.findViewById<ImageView>(R.id.spotifyFrame)
                        imageView?.setImageBitmap(bitmap)

                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody = errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("refresh error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Log.e("search error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(
                    call: Call<RequestDataInterface.getUserDataResponse>,
                    t: Throwable
                ) {
                    Log.e("search error: ", t.toString())
                }
            })


        } catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }

    }



}