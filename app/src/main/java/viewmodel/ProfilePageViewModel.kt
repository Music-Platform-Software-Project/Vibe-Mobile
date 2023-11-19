package viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.Item
import model.RequestDataInterface
import network.APIRequest
import network.RetrofitClient
import network.constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import view.LoginPage
import view.ProfilePage
import view.RecyclerViewAdapter
import view.ui.main.IncomingRequests

class ProfilePageViewModel() : ViewModel() {
    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: ProfilePage) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }



    fun setRecyclerView(){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.playlists_rec_view)
        recyclerView?.layoutManager =  GridLayoutManager(ctx, 3, GridLayoutManager.HORIZONTAL, false)

        val itemList = listOf(
            Item(R.drawable.vibe_logo_2, "Item 1"),
            Item(R.drawable.vibe_logo_2, "Item 2"),
            Item(R.drawable.vibe_logo_2, "Item 3"),
            // Add more items as needed
            Item(R.drawable.vibe_logo_2, "Item 4"),
            Item(R.drawable.vibe_logo_2, "Item 5"),
            Item(R.drawable.vibe_logo_2, "Item 6"),

            Item(R.drawable.vibe_logo_2, "Item 7"),
            Item(R.drawable.vibe_logo_2, "Item 8"),
            Item(R.drawable.vibe_logo_2, "Item 9"),
            // Add more items as needed
            Item(R.drawable.vibe_logo_2, "Item 10"),
            Item(R.drawable.vibe_logo_2, "Item 11"),
            Item(R.drawable.vibe_logo_2, "Item 12"),
        )

        val adapter = RecyclerViewAdapter(itemList, 1)
        recyclerView?.adapter = adapter


    }

    fun setRecyclerViewForArtists(){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.artists_rec_view)
        recyclerView?.layoutManager =  GridLayoutManager(ctx, 2, GridLayoutManager.HORIZONTAL, false)

        val itemList = listOf(
            Item(R.drawable.vibe_logo_2, "Artist 1"),
            Item(R.drawable.vibe_logo_2, "Artist 2"),
            Item(R.drawable.vibe_logo_2, "Artist 3"),
            // Add more items as needed
            Item(R.drawable.vibe_logo_2, "Artist 4"),
            Item(R.drawable.vibe_logo_2, "Artist 5"),
            Item(R.drawable.vibe_logo_2, "Artist 6"),

            Item(R.drawable.vibe_logo_2, "Artist 7"),
            Item(R.drawable.vibe_logo_2, "Artist 8"),
            Item(R.drawable.vibe_logo_2, "Artist 9"),
            // Add more items as needed
            Item(R.drawable.vibe_logo_2, "Artist 10"),
            Item(R.drawable.vibe_logo_2, "Artist 11"),
            Item(R.drawable.vibe_logo_2, "Artist 12"),
        )

        val adapter = RecyclerViewAdapter(itemList, 2)
        recyclerView?.adapter = adapter

    }

    fun setRecyclerViewForTracks(){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.tracks_rec_view)
        recyclerView?.layoutManager =  GridLayoutManager(ctx, 2, GridLayoutManager.HORIZONTAL, false)

        val itemList = listOf(
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            // Add more items as needed
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),

            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            // Add more items as needed
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
        )

        val adapter = RecyclerViewAdapter(itemList, 3)
        recyclerView?.adapter = adapter


    }

    fun sendRequest(username: String) {
        try {

            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)
            val request = RequestDataInterface.friendRequestPayload(username)

            val token = "Bearer " + constants.bearerToken
            val retrofitData = retrofitBuilder.sendRequest(token, request)
            Log.e("sending invitation", "going...")

            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("sending invitation:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("sending invitation: ",
                            (responseBody ?: "Response body is null").toString()
                        )
                        // Parse the responseBody as needed or handle the string response
                        if (responseBody != null) {
                            Log.e("sending invitation", "end")
                        }
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("sending error: ", "HTTP ${response.code()}: $errorBody")

                        } catch (e: Exception) {
                            Log.e("sending error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("sending error: ", t.toString())
                }
            })
        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }
}