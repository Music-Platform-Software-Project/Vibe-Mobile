package viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
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
import view.LoginPage
import view.PersonalizedTracks

class PersonalizedTracksViewModel() : ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient
    private val trackIdsList = mutableListOf<String>()
    private val accousticArr = mutableListOf<Int>()
    private val tempoArr = mutableListOf<Int>()
    private val instrumArr = mutableListOf<Int>()
    private val energyArr = mutableListOf<Int>()
    var accoustict1 = 0
    var ins1 = 0
    var temp1 = 0
    var energy1 = 0
    var accousAv = 0
    var tempoAv = 0
    var energyAv = 0
    var insAv = 0




    fun setContext(ctx: PersonalizedTracks) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun getOwnTracks(callback: (Double, Double, Double, Double) -> Unit) {
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
                        val responseBody = response.body()
                        Log.e("own track response:: ", responseBody.toString() ?: "Response body is null")
                        // Parse the responseBody as needed or handle the string response
                        if (responseBody != null) {
                            for (trackResponse in responseBody) {
                                trackIdsList.add(trackResponse.id)
                            }

                            Log.e("Track IDs:", trackIdsList.toString())

                            getTrackStats(trackIdsList) { accousticAvg, instrumAvg, tempoAvg, energyAvg ->
                                // This code will execute once the averages are available
                                // Add your logic here that depends on the averages
                                Log.e("Acoustic Average:", accousticAvg.toString())
                                Log.e("Instrum Average:", instrumAvg.toString())
                                Log.e("Tempo Average:", tempoAvg.toString())
                                Log.e("Energy Average:", energyAvg.toString())

                                // You can add any other logic that should execute after the averages are available
                                callback(accousticAvg, instrumAvg, tempoAvg, energyAvg)
                            }
                        }
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



    fun getTrackStats(trackIdList: List<String>, callback: (Double, Double, Double, Double) -> Unit) {
        val totalTracks = trackIdList.size // Total number of tracks
        var accousticSum = 0.0
        var instrumSum = 0.0
        var tempoSum = 0.0
        var energySum = 0.0

        for (id in trackIdList) {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl(constants.baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val retrofitBuilder = retrofit.create(APIRequest::class.java)

                val token = "Bearer " + constants.bearerToken
                val request = RequestDataInterface.TrackDetailRequest(id)
                val retrofitData = retrofitBuilder.getTrackDetails(token, request)
                Log.e("TrackStats process", "going...")

                retrofitData.enqueue(object : Callback<RequestDataInterface.TrackDetailResponse> {
                    override fun onResponse(
                        call: Call<RequestDataInterface.TrackDetailResponse>,
                        response: Response<RequestDataInterface.TrackDetailResponse>
                    ) {
                        Log.e("TrackStats response:", "retrieving body")
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            Log.e("TrackStats response: ", (responseBody ?: "Response body is null").toString())
                            accousticSum += responseBody!!.acousticness
                            instrumSum += responseBody!!.instrumentalness
                            tempoSum += responseBody!!.tempo
                            energySum += responseBody!!.energy

                            if (accousticArr.size == totalTracks) {
                                // Calculate the average for each array
                                val accousticAvg = accousticSum / totalTracks
                                val instrumAvg = instrumSum / totalTracks
                                val tempoAvg = tempoSum / totalTracks
                                val energyAvg = energySum / totalTracks

                                // Call the callback with the averages
                                callback(accousticAvg, instrumAvg, tempoAvg, energyAvg)
                            }
                        } else {
                            try {
                                var errorBody = response.errorBody()?.string()
                                errorBody = errorBody!!.substringAfter(":").trim()
                                errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                                Log.e("getAll error: ", "HTTP ${response.code()}: $errorBody")
                                Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Log.e("TrackStats error: ", "Error parsing error response.")
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<RequestDataInterface.TrackDetailResponse>,
                        t: Throwable
                    ) {
                        Log.e("TrackStats error: ", t.toString())

                        // Call the callback in case of failure as well
                        callback(0.0, 0.0, 0.0, 0.0)
                    }
                })

            } catch (e: Exception) {
                // Handle exceptions
                Log.e("error", e.toString())

                // Call the callback in case of an exception
                callback(0.0, 0.0, 0.0, 0.0)
            }
        }
    }


    fun acousticAverage(sum : Int, listSize : Int){

    }





}