package viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
import view.Dashboard
import view.RecyclerViewAdapter
import view.RegisterPage
import view.SearchPlaylist
import view.SearchTrack
import view.Settings
import view.my_room

class DashboardViewModel() : ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient


    fun setContext(ctx: Dashboard) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun setContext(ctx: my_room) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun switchToSettings(){
        val intent = Intent(ctx, Settings::class.java)
        ctx.startActivity(intent)
    }



    fun setRecyclerView(itemList: List<RequestDataInterface.MyPlaylistsResponse>){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.playlists_rec_view)
        val msg = (ctx as? Activity)?.findViewById<TextView>(R.id.favPlaylist)
        if(itemList.isEmpty()){
            recyclerView?.visibility =  View.INVISIBLE
            msg?.visibility = View.VISIBLE
        }
        else{
            recyclerView?.layoutManager =  GridLayoutManager(ctx, 3, GridLayoutManager.HORIZONTAL, false)
            val adapter = RecyclerViewAdapter(itemList, 1)
            recyclerView?.adapter = adapter
            recyclerView?.visibility =  View.VISIBLE
            msg?.visibility = View.INVISIBLE
        }
    }


    fun setRecyclerViewForArtists(itemList: List<RequestDataInterface.MyPlaylistsResponse>){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.artists_rec_view)
        val msg = (ctx as? Activity)?.findViewById<TextView>(R.id.favArtist)
        if(itemList.isEmpty()){
            recyclerView?.visibility =  View.INVISIBLE
            msg?.visibility = View.VISIBLE
        }
        else{
            recyclerView?.layoutManager =  GridLayoutManager(ctx, 2, GridLayoutManager.HORIZONTAL, false)

            val adapter = RecyclerViewAdapter(itemList, 2)
            recyclerView?.adapter = adapter
            recyclerView?.visibility =  View.VISIBLE
            msg?.visibility = View.INVISIBLE
        }
    }

    fun setRecyclerViewForTracks(itemList: List<RequestDataInterface.MyPlaylistsResponse>){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.tracks_rec_view)
        val msg = (ctx as? Activity)?.findViewById<TextView>(R.id.favTrack)
        if(itemList.isEmpty()){
            recyclerView?.visibility =  View.INVISIBLE
            msg?.visibility = View.VISIBLE
        }
        else{
            recyclerView?.layoutManager =  GridLayoutManager(ctx, 2, GridLayoutManager.HORIZONTAL, false)

            val adapter = RecyclerViewAdapter(itemList, 3)
            recyclerView?.adapter = adapter
            recyclerView?.visibility =  View.VISIBLE
            msg?.visibility = View.INVISIBLE
        }
    }

    fun addPlaylist(userInput: String) {
        try {
            val retrofitBuilder = retrofitClient.createAPIRequest()

            val request = RequestDataInterface.addPlaylistRequest(userInput)
            val token = "Bearer " + constants.bearerToken
            val retrofitData = retrofitBuilder.addPlaylist(token, request)
            Log.e("playlist process", "going...")

            retrofitData.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.e("playlist response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("playlist response: ",
                            (responseBody ?: "Response body is null").toString()
                        )
                        refreshPlaylists()
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("login error: ", "HTTP ${response.code()}: $errorBody")
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



    fun refreshPlaylists(){
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)

            val token = "Bearer " + constants.bearerToken
            val retrofitData = retrofitBuilder.getOwnPlaylists(token)

            retrofitData.enqueue(object : Callback<List<RequestDataInterface.MyPlaylistsResponse>> {
                override fun onResponse(call: Call<List<RequestDataInterface.MyPlaylistsResponse>>, response: Response<List<RequestDataInterface.MyPlaylistsResponse>>) {
                    Log.e("refresh response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                       Log.e("refresh response: ",
                            (responseBody ?: "Response body is null").toString()
                        )
                        setRecyclerView(response.body()!!)
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("refresh error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Log.e("refresh error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<List<RequestDataInterface.MyPlaylistsResponse>>, t: Throwable) {
                    Log.e("refresh error: ", t.toString())
                }
            })

        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }



    // COMMENT THIS FUNCTION OUT AFTER THE MVP
// In your ViewModel
    fun isThereALikedPL(callback: (Int) -> Unit) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)

            val token = "Bearer " + constants.bearerToken
            val retrofitData = retrofitBuilder.getOwnPlaylists(token)

            retrofitData.enqueue(object : Callback<List<RequestDataInterface.MyPlaylistsResponse>> {
                override fun onResponse(
                    call: Call<List<RequestDataInterface.MyPlaylistsResponse>>,
                    response: Response<List<RequestDataInterface.MyPlaylistsResponse>>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val counter = if (responseBody != null && responseBody.isNotEmpty()) {
                            responseBody.size // Count of liked playlists
                        } else {
                            0 // No liked playlists
                        }
                        // Call the callback with the counter value
                        callback(counter)
                    } else {
                        // Handle error response
                        callback(0) // No liked playlists
                    }
                }

                override fun onFailure(call: Call<List<RequestDataInterface.MyPlaylistsResponse>>, t: Throwable) {
                    // Handle failure...
                    callback(0) // No liked playlists
                }
            })

        } catch (e: Exception) {
            // Handle exceptions...
            callback(0) // No liked playlists
        }
    }








    fun switchToSearchPlaylist(){
        val intent = Intent(ctx, SearchPlaylist::class.java)
        ctx.startActivity(intent)
    }

    fun switchToSearchTrack(){
        val intent = Intent(ctx, SearchTrack::class.java)
        ctx.startActivity(intent)
    }



}