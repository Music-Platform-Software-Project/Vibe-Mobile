package viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cs308_00.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
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
import view.PersonalizedTracks

class PersonalizedTracksViewModel() : ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient
    private val _averageTempo = MutableLiveData<Double>()
    private val _averageInstrumentalness = MutableLiveData<Double>()
    private val _averageAcousticness = MutableLiveData<Double>()
    private val _averageEnergy = MutableLiveData<Double>()




    fun setContext(ctx: PersonalizedTracks) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }



    // Expose LiveData properties to observe in the activity
    val averageTempo: LiveData<Double>
        get() = _averageTempo

    val averageInstrumentalness: LiveData<Double>
        get() = _averageInstrumentalness

    val averageAcousticness: LiveData<Double>
        get() = _averageAcousticness

    val averageEnergy: LiveData<Double>
        get() = _averageEnergy


    fun getStatistics(days : Int){
        try {
            val retrofitBuilder = retrofitClient.createAPIRequestWithToken(constants.bearerToken)
            val request = RequestDataInterface.UserStatsRequest(days)
            val token = "Bearer " + constants.bearerToken
            val retrofitData = retrofitBuilder.userStatistics(token, request)
            retrofitData.enqueue(object : Callback<List<RequestDataInterface.UserStatResponse>> {
                override fun onResponse(call: Call<List<RequestDataInterface.UserStatResponse>>, response: Response<List<RequestDataInterface.UserStatResponse>>) {
                    Log.e("login response:", "retrieving body")
                    if (response.isSuccessful) {
                        var responseBody = response.body()
                        Log.e("stat response: ", responseBody.toString() ?: "Response body is null")
                        //Toast.makeText(ctx, "Track ${trackData.track.name} imported successfully !", Toast.LENGTH_LONG).show()
                        val averageTempoValue = responseBody!!.map { it.track.tempo }.average()
                        Log.e("averageTempoValue vm",averageTempoValue.toString() )
                        val averageInstrumentalnessValue = responseBody.map { it.track.instrumentalness }.average()
                        Log.e("averageInstrumental vm ",averageInstrumentalnessValue.toString() )
                        val averageAcousticnessValue = responseBody.map { it.track.acousticness }.average()
                        Log.e("averageAcousticness vm",averageAcousticnessValue.toString() )
                        val averageEnergyValue = responseBody.map { it.track.energy }.average()
                        Log.e("averageEnergyValue vm",averageEnergyValue.toString() )

                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("stat error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Log.e("stat error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<List<RequestDataInterface.UserStatResponse>>, t: Throwable) {
                    Log.e("stat error: ", t.toString())
                }
            })

        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }

    }








}