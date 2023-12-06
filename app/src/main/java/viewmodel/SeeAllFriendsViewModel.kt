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
import view.ProfilePage
import view.RegisterPage
import view.SeeAllFriends

class SeeAllFriendsViewModel() : ViewModel() {
    private lateinit var ctx: Context
    //private lateinit var retrofitClient: RetrofitClient
    private var responseBodyGen : RequestDataInterface.getUserDataResponse? = null

    fun setContext(ctx: SeeAllFriends) {
        this.ctx = ctx
        //retrofitClient = RetrofitClient(ctx)
    }

    fun goBack(){
        val i = Intent(ctx, ProfilePage::class.java)
        ctx.startActivity(i)
    }

    fun seeAllFriends(){

        try{

            val recViewList: MutableList<String> = mutableListOf()
            val adapter = SeeAllFriendsRecViewAdapter(ctx, recViewList, SeeAllFriendsViewModel())
            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)
            val token = "Bearer " + constants.bearerToken
            Log.e("bearer set username", constants.bearerToken)
            val retrofitData = retrofitBuilder.userGetData(token)
            Log.e("username process", "Going...")

            retrofitData.enqueue(object : Callback<RequestDataInterface.getUserDataResponse> {
                override fun onResponse(call: Call<RequestDataInterface.getUserDataResponse>, response: Response<RequestDataInterface.getUserDataResponse>) {
                    Log.e("set username response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body().toString()
                        val userHolder = (ctx as? Activity)?.findViewById<TextView>(R.id.userNameHolder)
                        userHolder!!.text = response.body()!!.username
                        responseBodyGen = response.body()
                        val friendsList = response.body()!!.friends ?: emptyList()


                        // Extract friend usernames
                        for (friend in friendsList) {
                            constants.friendsList.add(friend)
                            Log.e("in see all friends", constants.friendsList.toString())
                            val username = friend.username
                            recViewList.add(username)
                        }

                        Log.e("user get response: ", responseBody ?: "Response body is null")
                        // Parse the responseBody as needed or handle the string response

                        val recView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.friendsRecView)
                        recView?.layoutManager = LinearLayoutManager(ctx)

                        recView?.adapter = adapter


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

    fun removeFriend(username: String){
        val currFriends : List<RequestDataInterface.friends> = constants.friendsList
        Log.e("friends in remove:", currFriends.toString())
        for (friend in currFriends){
            Log.e("friends in remove:", currFriends.toString())
            Log.e("username param:", username)
            Log.e("data coming:", friend.username)
            if (friend.username == username){
                val friendId = friend.id
                Log.e("id ", friendId)
                try{
                    val retrofit = Retrofit.Builder()
                        .baseUrl(constants.baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val retrofitBuilder = retrofit.create(APIRequest::class.java)
                    val token = "Bearer " + constants.bearerToken
                    Log.e("bearer set username", constants.bearerToken)
                    val request = RequestDataInterface.removeFriendRequest(friendId)
                    val retrofitData = retrofitBuilder.removeFriend(token, request)
                    Log.e("getFriends process", "Going...")

                    retrofitData.enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            Log.e("remove friend response:", "retrieving body")
                            if (response.isSuccessful) {
                                val responseBody = response.body().toString()
                                seeAllFriends()
                               // Toast.makeText(ctx, "Friend '${username}' successfully removed !", Toast.LENGTH_LONG).show()
                                //val friendsList = response.body()!!.friends ?: emptyList()
                                Log.e("removefriend response: ", responseBody ?: "Response body is null")
                                // Parse the responseBody as needed or handle the string response



                            }
                            else {
                                try {
                                    var errorBody = response.errorBody()?.string()
                                    errorBody =  errorBody!!.substringAfter(":").trim()
                                    errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                                    Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                                    Log.e("remove friend error: ", "HTTP ${response.code()}: $errorBody")


                                } catch (e: Exception) {
                                    Log.e("remove friend error: ", "Error parsing error response.")
                                }
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.e("remove friend error: ", t.toString())
                        }
                    })

                }

                catch (e: Exception) {
                    Log.e("error", e.toString())
                    // Handle the exception here (e.g. log it or display an error message)
                }

            }
        }

    }


}