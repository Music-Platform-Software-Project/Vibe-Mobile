package viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import com.nitish.typewriterview.TypeWriterView
import com.squareup.picasso.Picasso
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
import view.LikedRecViewAdapter
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

    fun selectRoomSong(){
        val intent = Intent(ctx, SearchTrack::class.java)
        ctx.startActivity(intent)
    }

    fun setRoomSong(){
        try {

            val roomFrame : ImageView = (ctx as? Activity)?.findViewById<ImageView>(R.id.roomFrame)!!
            var base64Image : String = ""



            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)
            val token = "Bearer " + constants.bearerToken
            Log.e("bearer set username", constants.bearerToken)
            val retrofitData = retrofitBuilder.userGetData(token)
            Log.e("myroom process", "Going...")

            retrofitData.enqueue(object : Callback<RequestDataInterface.getUserDataResponse> {
                override fun onResponse(
                    call: Call<RequestDataInterface.getUserDataResponse>,
                    response: Response<RequestDataInterface.getUserDataResponse>
                ) {
                    Log.e("my room response:", response.message())
                    if (response.isSuccessful) {
                        val responseBody = response.body().toString()
                       val track = (ctx as? Activity)?.findViewById<TypeWriterView>(R.id.personalRoomTrack)
                        track!!.setCharacterDelay(150)
                        track.animateText(response.body()!!.selectedTrack.name)
                        //track!!.text = response.body()!!.selectedTrack.name
                        Log.e("my room response: ", responseBody ?: "Response body is null")

                        base64Image = response.body()!!.selectedRoom.image

                        val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
                        // Decode the byte array to a Bitmap
                        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        // Set the Bitmap to the ImageView
                        roomFrame.setImageBitmap(bitmap)



                        //DUMMY ENDPOINT BITINCE BURAYI SIL USTTEKINI AC TODO
                        /*
                        val decodedBytesDummy = Base64.decode(constants.dummyBase64, Base64.DEFAULT)
                        val bitmapDummy = BitmapFactory.decodeByteArray(decodedBytesDummy, 0, decodedBytesDummy.size)
                        roomFrame.setImageBitmap(bitmapDummy)
                         */


                        if (responseBody.isEmpty()){
                            track!!.setCharacterDelay(150)
                            track.animateText("You haven!t selected a song for your room yet")
                            val addRoomSong = (ctx as? Activity)?.findViewById<Button>(R.id.addRoomSong)
                            addRoomSong!!.visibility = View.VISIBLE
                        }
                        // Parse the responseBody as needed or handle the string response

                    } else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody = errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("username error: ", "HTTP ${response.code()}: $errorBody")


                        } catch (e: Exception) {
                            Log.e("set username error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(
                    call: Call<RequestDataInterface.getUserDataResponse>,
                    t: Throwable
                ) {
                    Log.e("registration error: ", t.toString())
                }
            })

        } catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }



    //FOR LIKED SONGS
    fun setRecyclerView(itemList: List<RequestDataInterface.MyPlaylistsResponse>){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.playlists_rec_view)
        val msg = (ctx as? Activity)?.findViewById<TextView>(R.id.favPlaylist)
        if(itemList.isEmpty()){
            recyclerView?.visibility =  View.INVISIBLE
            msg?.visibility = View.VISIBLE
        }
        else{
            recyclerView?.layoutManager =  GridLayoutManager(ctx, 3, GridLayoutManager.HORIZONTAL, false)
            val adapter = LikedRecViewAdapter(itemList, 1)
            recyclerView?.adapter = adapter
            recyclerView?.visibility =  View.VISIBLE
            msg?.visibility = View.INVISIBLE
        }
    }

    //FOR PLAYLISTS
    fun setRecyclerViewPlaylist(itemList: List<RequestDataInterface.MyPlaylistsResponse>){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.playlists_rec_view2)
        val msg = (ctx as? Activity)?.findViewById<TextView>(R.id.favPlaylist2)
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


    fun addRealPlaylist(userInput: String) {
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
                        refreshRealPlaylists()
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


    //FOR PLAYLISTS
    fun refreshRealPlaylists(){
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
                        setRecyclerViewPlaylist(response.body()!!)
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

    //FOR LIKED SONGS
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
                       Log.e("refresh response: ", (responseBody ?: "Response body is null").toString())
                        setRecyclerView(response.body()!!)
                        for (item in response.body()!!){
                            if(item.name == "Liked Songs"){
                                constants.likedSongsId = item.id
                            }
                        }
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