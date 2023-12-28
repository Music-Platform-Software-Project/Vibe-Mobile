package view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import model.RequestDataInterface
import network.APIRequest
import network.constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import viewmodel.PersonalizedTracksViewModel
import viewmodel.SearchTrackRecViewAdapter


class PersonalizedTracks : AppCompatActivity() {
    private lateinit var viewModel: PersonalizedTracksViewModel

    lateinit var barChart: BarChart

    // on below line we are creating
    // a variable for bar data
    lateinit var barData: BarData

    // on below line we are creating a
    // variable for bar data set
    lateinit var barDataSet: BarDataSet

    // on below line we are creating array list for bar data
    lateinit var barEntriesList: ArrayList<BarEntry>
    //lateinit var barEntriesList: ArrayList<BarEntry>

    private var averageTempo: Double = 0.0
    private var averageInstrumentalness: Double = 0.0
    private var averageAcousticness: Double = 0.0
    private var averageEnergy: Double = 0.0
    private var currentTime = 0





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalized_tracks)
        barChart = findViewById(R.id.barChart);

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel = ViewModelProvider(this).get(PersonalizedTracksViewModel::class.java)
        viewModel.setContext(this)

        getStatistics(7)
        val changeButton : ImageView = findViewById(R.id.changeTime)
        val dynamicBannerView = findViewById<ImageView>(R.id.dynamicBannerImageView)







        val share : Button = findViewById(R.id.shareResults)
        share.setOnClickListener {
            // Invalidate the chart to ensure it is fully drawn.
            barChart.invalidate()

            // Wait for the chart to be fully drawn before capturing the bitmap.
            barChart.post {
                val chartWidth = 900 // Width in pixels
                val chartHeight = 600 // Height in pixels
                val chartBitmap = captureChartAsBitmap(barChart, chartWidth, chartHeight)

                // Create an intent to share the captured chart.
                val intent = Intent(Intent.ACTION_SEND)
                val path = MediaStore.Images.Media.insertImage(contentResolver, chartBitmap, "Bar Chart", null)
                val uri = Uri.parse(path)
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                intent.type = "image/*"
                startActivity(Intent.createChooser(intent, "Share To: "))
            }
        }

        val text : TextView = findViewById(R.id.textView2)
        changeButton.setOnClickListener {
            if(currentTime == 0){
                getStatistics(30)
                text.text = "Average for the Past 30 Days"
                currentTime = 1
            }
            else if(currentTime == 1){
                getStatistics(7)
                text.text = "Average for the Past 7 Days"
                currentTime = 0
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Respond to the action bar's Up/Home button
                onBackPressed() // This will call the default behavior of going back
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getBarChartData(averageTempo: Double, averageInstrumentalness: Double, averageAcousticness: Double, averageEnergy: Double) {
        barEntriesList = ArrayList()

        // Use the passed average values to create BarEntry objects.
        barEntriesList.add(BarEntry(1f, averageTempo.toFloat()))
        barEntriesList.add(BarEntry(2f, averageInstrumentalness.toFloat()))
        barEntriesList.add(BarEntry(3f, averageAcousticness.toFloat()))
        barEntriesList.add(BarEntry(4f, averageEnergy.toFloat()))
        barDataSet = BarDataSet(barEntriesList, "Bar Chart Data")

        /*
        // on below line we are initializing our bar data
        barData = BarData(barDataSet)

        // on below line we are setting data to our bar chart
        barChart.data = barData

        // on below line we are setting colors for our bar chart text
        barDataSet.valueTextColor = Color.WHITE

        // on below line we are setting color for our bar data set
        barDataSet.setColor(resources.getColor(R.color.purple_200))

        // on below line we are setting text size
        barDataSet.valueTextSize = 16f

        // on below line we are enabling description as false
        barChart.description.isEnabled = false

        // ...

         */
    }




    fun getStatistics(days : Int){
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)

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
                         averageTempo = responseBody!!.map { it.track.tempo }.average()
                        Log.e("averageTempoValue vm",averageTempo.toString() )
                        averageInstrumentalness = responseBody.map { it.track.instrumentalness }.average()
                        Log.e("averageInstrumental vm ",averageInstrumentalness.toString() )
                        averageAcousticness = responseBody.map { it.track.acousticness }.average()
                        Log.e("averageAcousticness vm",averageAcousticness.toString() )
                        averageEnergy = responseBody.map { it.track.energy }.average()
                        Log.e("averageEnergyValue vm",averageEnergy.toString() )
                        generateBarChart(barChart, averageTempo, averageInstrumentalness, averageAcousticness, averageEnergy)

                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("stat error: ", "HTTP ${response.code()}: $errorBody")
                            //Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
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



    fun generateBarChart(
        barChart: BarChart,
        averageTempo: Double,
        averageInstrumentalness: Double,
        averageAcousticness: Double,
        averageEnergy: Double
    ) {
        // Create a list of BarEntry with the average values.
        val barEntries = mutableListOf(
            BarEntry(0f, averageTempo.toFloat(), "Tempo"),
            BarEntry(1f, averageInstrumentalness.toFloat(), "Instrumentalness"),
            BarEntry(2f, averageAcousticness.toFloat(), "Acousticness"),
            BarEntry(3f, averageEnergy.toFloat(), "Energy")
        )

        // Create a BarDataSet with BarEntries and set label.
        val dataSet = BarDataSet(barEntries, "")

        // Customize the appearance of the bar chart.
        dataSet.color = barChart.context.resources.getColor(R.color.purple_200)
        dataSet.valueTextColor = Color.WHITE

        val data = BarData(dataSet)

        data.barWidth = 0.4f
        // Set data to the bar chart.
        barChart.data = data
        barChart.invalidate()

        // Customize the bar chart further as needed.
        val xAxis = barChart.xAxis
        xAxis.setDrawLabels(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)


        // Set custom labels for the X-axis.

        val labels = listOf("Tempo", "Instrumentalness", "Acousticness", "Energy")
        xAxis.run {
            setLabelCount(labels.size, true) // Set the label count and enable auto-adjustment
            //labelRotationAngle = 45f // Set the rotation angle for better visibility
            valueFormatter = IndexAxisValueFormatter(labels)
        }

        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.axisMinimum = -0.5f
        xAxis.axisMaximum = labels.size - 0.5f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)




        // Customize the numbers on the horizontal part of the chart.
        xAxis.textColor = Color.WHITE
        barChart.axisLeft.textColor = Color.WHITE
        barChart.axisRight.textColor = Color.WHITE

        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toString()
            }
        })

        barChart.setFitBars(true)
        barChart.description.isEnabled = false
        barChart.invalidate()
    }


    private fun captureChartAsBitmap(barChart: BarChart, width: Int, height: Int): Bitmap {
        // Create a bitmap with the desired width and height.
        val chartBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Create a canvas with the bitmap.
        val canvas = Canvas(chartBitmap)

        // Set the chart's dimensions to match the bitmap's size.
        barChart.layout(0, 0, width, height)

        // Draw the chart onto the canvas.
        barChart.draw(canvas)

        return chartBitmap
    }








}