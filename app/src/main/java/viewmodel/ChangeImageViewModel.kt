package viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import view.ChangeRoomImage


class ChangeImageViewModel() : ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient
    fun setContext(ctx: ChangeRoomImage) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)

    }



    fun getImages(){
        try {
            val retrofitBuilder = retrofitClient.createAPIRequestWithToken(constants.bearerToken)
            val retrofitData = retrofitBuilder.getImages(constants.bearerToken)
            val imageList: MutableList<RequestDataInterface.ImageData> = mutableListOf()
            retrofitData.enqueue(object : Callback<List<RequestDataInterface.ImageData>> {
                override fun onResponse(
                    call: Call<List<RequestDataInterface.ImageData>>,
                    response: Response<List<RequestDataInterface.ImageData>>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("image response: ", (responseBody ?: "Response body is null").toString())
                        if(!responseBody?.isEmpty()!!){
                            responseBody?.let {
                                imageList.addAll(it)
                            }

                            val recView : RecyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.imagesRecView)!!
                            val adapter = RoomImageAdapter(ctx, imageList)

                            recView.layoutManager = LinearLayoutManager(ctx)
                            recView.adapter = adapter

                        }
                    } else {
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
                    call: Call<List<RequestDataInterface.ImageData>>,
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