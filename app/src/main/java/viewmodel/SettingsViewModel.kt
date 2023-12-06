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
import com.google.android.material.color.utilities.TonalPalette
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
import view.LoginPage
import view.Settings
import view.my_room

class SettingsViewModel() : ViewModel() {
    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient


    fun setContext(ctx: Settings) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun backToDashboard() {
        val intent = Intent(ctx, Dashboard::class.java)
        ctx.startActivity(intent)
    }


    private fun exitApp() {
        val intent = Intent(ctx, LoginPage::class.java)
        ctx.startActivity(intent)
    }


    fun getDetails() {

        try {

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
                override fun onResponse(
                    call: Call<RequestDataInterface.getUserDataResponse>,
                    response: Response<RequestDataInterface.getUserDataResponse>
                ) {
                    Log.e("set username response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body().toString()
                        val username =
                            (ctx as? Activity)?.findViewById<TextView>(R.id.usernameInput)
                        username!!.text = response.body()!!.username
                        val email = (ctx as? Activity)?.findViewById<TextView>(R.id.emailInput)
                        val numOfFriends =
                            (ctx as? Activity)?.findViewById<TextView>(R.id.numOfFriendsInput)
                        email!!.text = response.body()!!.email
                        val friendsList = response.body()!!.friends ?: emptyList()
                        numOfFriends!!.text = friendsList.count().toString()
                        Log.e("user get response: ", responseBody ?: "Response body is null")
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

    fun updateUsername(username: String) {
        try {
            val retrofitBuilder = retrofitClient.createAPIRequestWithToken(constants.bearerToken)
            val request = RequestDataInterface.UpdateUserRequest(username)
            val retrofitData = retrofitBuilder.updateUser(request)
            Log.e("registration process", "going...")
            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("registration response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("registration response: ", responseBody ?: "Response body is null")
                        // Parse the responseBody as needed or handle the string response
                        Toast.makeText(
                            ctx,
                            "Username successfully updated to $username",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody = errorBody!!.substring(1, errorBody.length - 1)
                            Log.e("registration error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()

                        } catch (e: Exception) {
                            Log.e("registration error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("registration error: ", t.toString())
                }
            })


        } catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }

    fun getOwnTracks() {
        try {
            val retrofitBuilder = retrofitClient.createAPIRequest()
            val token = "Bearer " + constants.bearerToken
            val retrofitData = retrofitBuilder.getOwnTracks(token)

            Log.e("registration process", "going...")
            retrofitData.enqueue(object : Callback<List<RequestDataInterface.GetOwnTrackResponse>> {
                override fun onResponse(
                    call: Call<List<RequestDataInterface.GetOwnTrackResponse>>,
                    response: Response<List<RequestDataInterface.GetOwnTrackResponse>>
                ) {
                    Log.e("own track response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body().toString()
                        Log.e("own track response:: ", responseBody ?: "Response body is null")
                        // Parse the responseBody as needed or handle the string response
                        val numTrack = (ctx as? Activity)?.findViewById<TextView>(R.id.trackNumInput)
                        numTrack!!.text = response.body()!!.size.toString()


                    } else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody = errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("registration error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()

                        } catch (e: Exception) {
                            Log.e("own track response:: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<RequestDataInterface.GetOwnTrackResponse>>,
                    t: Throwable
                ) {
                    Log.e("own track error: ", t.toString())
                }
            })

        } catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }


    fun deleteUser() {
        try {
            val retrofitBuilder = retrofitClient.createAPIRequestWithToken(constants.bearerToken)
            val retrofitData = retrofitBuilder.deleteUser()
            Log.e("registration process", "going...")
            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("registration response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body().toString()
                        Log.e("deletion response: ", responseBody ?: "Response body is null")
                        // Parse the responseBody as needed or handle the string response
                        Toast.makeText(ctx, "User Account Successfully Deleted", Toast.LENGTH_LONG)
                            .show()
                        exitApp()

                    } else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody = errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("registration error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()

                        } catch (e: Exception) {
                            Log.e("registration error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("registration error: ", t.toString())
                }
            })

        } catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }
}

