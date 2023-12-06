package viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
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
import view.ImportTrack
import view.LoginPage
import view.ProfilePage
import view.RecyclerViewAdapter
import view.RegisterPage
import view.SeeAllFriends
import view.ui.main.IncomingRequests

class ProfilePageViewModel() : ViewModel() {
    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient
    private var checker : Boolean = false

    fun setContext(ctx: ProfilePage) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }



    fun setRecyclerView(itemList: List<RequestDataInterface.MyPlaylistsResponse>){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.playlists_rec_view)
        recyclerView?.layoutManager =  GridLayoutManager(ctx, 3, GridLayoutManager.HORIZONTAL, false)

        val adapter = RecyclerViewAdapter(itemList, 1)
        recyclerView?.adapter = adapter


    }

    fun setRecyclerViewForArtists(itemList: List<RequestDataInterface.MyPlaylistsResponse>){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.artists_rec_view)
        recyclerView?.layoutManager =  GridLayoutManager(ctx, 2, GridLayoutManager.HORIZONTAL, false)

        val adapter = RecyclerViewAdapter(itemList, 2)
        recyclerView?.adapter = adapter

    }

    fun setRecyclerViewForTracks(itemList: List<RequestDataInterface.MyPlaylistsResponse>){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.tracks_rec_view)
        recyclerView?.layoutManager =  GridLayoutManager(ctx, 2, GridLayoutManager.HORIZONTAL, false)



        val adapter = RecyclerViewAdapter(itemList, 3)
        recyclerView?.adapter = adapter
    }

    fun switchToImportTrack(){
        val i = Intent(ctx, ImportTrack::class.java)
        ctx.startActivity(i)
    }

    fun switchToSeeAllFriends(){
        val i = Intent(ctx, SeeAllFriends::class.java)
        ctx.startActivity(i)
    }
    fun setUsernameandFriends(){
        try{
            val retrofitBuilder = retrofitClient.createAPIRequest()
            val token = "Bearer " + constants.bearerToken
            val retrofitData = retrofitBuilder.userGetData(token)
            Log.e("username process", "Going...")
            retrofitData.enqueue(object : Callback<RequestDataInterface.getUserDataResponse> {
                override fun onResponse(call: Call<RequestDataInterface.getUserDataResponse>, response: Response<RequestDataInterface.getUserDataResponse>) {
                    Log.e("set username response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body().toString()
                        Log.e("user get response: ", responseBody ?: "Response body is null")
                        val username = (ctx as? Activity)?.findViewById<TextView>(R.id.profileUsername)
                        username!!.text = response.body()!!.username
                        if(response.body()!!.friends.isNotEmpty())
                        {
                            checker = true
                            if (response.body()!!.friends.size == 1){
                                val friend1 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername1)
                                friend1!!.text = response.body()!!.friends[0].username
                            }
                            else{
                                val friend1 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername1)
                                friend1!!.text = "No Friend :("
                            }
                            if (response.body()!!.friends.size == 2){
                                val friend2 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername2)
                                friend2!!.text = response.body()!!.friends[1].username
                            }
                            else{
                                val friend2 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername2)
                                friend2!!.text ="No Friend :("
                            }
                            if (response.body()!!.friends.size == 3){
                                val friend3 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername3)
                                friend3!!.text = response.body()!!.friends[2].username
                            }

                            else{
                                val friend3 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername3)
                                friend3!!.text = "No Friend :("
                            }
                            if (response.body()!!.friends.size == 4){
                                val friend4 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername4)
                                friend4!!.text = response.body()!!.friends[3].username
                            }
                            else{
                                val friend4 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername4)
                                friend4!!.text ="No Friend :("
                            }


                        }
                        else{
                            val friendsHolder1 = (ctx as? Activity)?.findViewById<LinearLayout>(R.id.friendsHolder1)
                            friendsHolder1!!.visibility = View.INVISIBLE
                            val friendsHolder2 = (ctx as? Activity)?.findViewById<LinearLayout>(R.id.friendsHolder2)
                            friendsHolder2!!.visibility = View.INVISIBLE
                            val friendsHolder3 = (ctx as? Activity)?.findViewById<LinearLayout>(R.id.friendsHolder3)
                            friendsHolder3!!.visibility = View.INVISIBLE
                            val friendsHolder4 = (ctx as? Activity)?.findViewById<LinearLayout>(R.id.friendsHolder4)
                            friendsHolder4!!.visibility = View.INVISIBLE
                            val noFriend = (ctx as? Activity)?.findViewById<TextView>(R.id.noFriends)
                            noFriend!!.visibility = View.VISIBLE

                        }

                        // Parse the responseBody as needed or handle the string response

                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("username error: ", "HTTP ${response.code()}: $errorBody")


                        } catch (e: Exception) {
                            Log.e("set username error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<RequestDataInterface.getUserDataResponse>, t: Throwable) {
                    Log.e("registration error: ", t.toString())
                }
            })
        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }


    fun checkForFriends(){
        if (checker){
            switchToSeeAllFriends()
        }
        else{
            Toast.makeText(ctx, "You Do Not Have Any Friends To Display", Toast.LENGTH_LONG).show()
        }

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