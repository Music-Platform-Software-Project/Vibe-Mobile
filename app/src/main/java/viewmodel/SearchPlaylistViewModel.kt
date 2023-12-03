package viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
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

import view.SearchPlaylist

class SearchPlaylistViewModel() : ViewModel() {
    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: SearchPlaylist) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun switchToDashboard(){
        val i = Intent(ctx, Dashboard::class.java)
        ctx.startActivity(i)
    }

    fun searchPlaylist(searchKey : String){
        try {
            val retrofitBuilder = retrofitClient.createAPIRequestWithToken(constants.bearerToken)
            val request = RequestDataInterface.PlaylistTrackSearchRequest(searchKey)
            val retrofitData = retrofitBuilder.searchPlaylist(request)
            val plList: MutableList<RequestDataInterface.SearchPlaylistResponse> = mutableListOf()
            retrofitData.enqueue(object :
                Callback<List<RequestDataInterface.SearchPlaylistResponse>> {
                override fun onResponse(call: Call<List<RequestDataInterface.SearchPlaylistResponse>>, response: Response<List<RequestDataInterface.SearchPlaylistResponse>>) {
                    Log.e("refresh response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("refresh response: ", (responseBody ?: "Response body is null").toString())

                        responseBody?.let {
                            plList.addAll(it)
                            // Create the adapter and set it to the RecyclerView
                            val recView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.searchPlaylistRecView)
                            recView?.layoutManager = LinearLayoutManager(ctx)
                            val adapter = SearchPlaylistRecViewAdapter(ctx, plList)
                            recView?.adapter = adapter
                            recView?.layoutManager = GridLayoutManager(ctx, 2)
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

                override fun onFailure(call: Call<List<RequestDataInterface.SearchPlaylistResponse>>, t: Throwable) {
                    Log.e("refresh error: ", t.toString())
                }
            })



        }

        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }
}