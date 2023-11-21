package viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface
import network.RetrofitClient
import network.constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import view.RegisterPage
import view.SeeAllFriends

class SeeAllFriendsViewModel() : ViewModel() {
    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient
    private var responseBodyGen : RequestDataInterface.getUserDataResponse? = null

    fun setContext(ctx: SeeAllFriends) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun goBack(){
        val i = Intent(ctx, ProfilePageViewModel::class.java)
        ctx.startActivity(i)
    }

    fun seeAllFriends(){
        try{
            val retrofitBuilder = retrofitClient.createAPIRequest()
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
                        val recViewList: MutableList<String> = mutableListOf()
                        // Extract friend usernames
                        for (friend in friendsList) {
                            val username = friend.username
                            recViewList.add(username)
                        }

                        Log.e("user get response: ", responseBody ?: "Response body is null")
                        // Parse the responseBody as needed or handle the string response
                        val recView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.friendsRecView)
                        recView?.layoutManager = LinearLayoutManager(ctx)
                        val adapter = SeeAllFriendsRecViewAdapter(ctx, recViewList)
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

}