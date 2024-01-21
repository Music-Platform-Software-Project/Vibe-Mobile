package viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
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
import view.ProfilePage
import view.RecyclerViewAdapter
import view.SeeAllFriends

class ProfilePageViewModel() : ViewModel() {
    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient
    private var checker : Boolean = false
    private var friendsList = mutableListOf<RequestDataInterface.friends>()
    private lateinit var currFriends : List<RequestDataInterface.friends>
    private val numberOfIntermediatePoints = 10 // Number of points between each data point
    private var job: Job? = null
    private val animationScope = CoroutineScope(Dispatchers.Default)

    fun setContext(ctx: ProfilePage) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun updateUI() {
        // Call the function to set the username and friends data
        setUsernameandFriends()
        // Add any other UI updates you need here
    }

    fun getDynamicBannerData(id : String, lineChart : LineChart){
        try {

            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)
            val request = RequestDataInterface.PlaylistAverageRequest(id)

            val token = "Bearer " + constants.bearerToken
            val retrofitData = retrofitBuilder.getDynamicBannerData(token, request)
            Log.e("sending invitation", "going...")

            retrofitData.enqueue(object : Callback<RequestDataInterface.PlaylistAverageResponse> {
                override fun onResponse(call: Call<RequestDataInterface.PlaylistAverageResponse>, response: Response<RequestDataInterface.PlaylistAverageResponse>) {
                    Log.e("sending invitation:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("sending invitation: ",
                            (responseBody ?: "Response body is null").toString()
                        )
                        // Parse the responseBody as needed or handle the string response
                        if (responseBody != null) {
                            Log.e("dynamic banner av",  response.body()!!.tempoAverage.toString() + " "  + response.body()!!.instrumentalnessAverage.toString())
                            generateLineChart(lineChart, response.body()!!.tempoAverage, response.body()!!.instrumentalnessAverage.toDouble(),
                                response.body()!!.acousticnessAverage.toDouble(),response.body()!!.energyAverage.toDouble())
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

                override fun onFailure(call: Call<RequestDataInterface.PlaylistAverageResponse>, t: Throwable) {
                    Log.e("sending error: ", t.toString())
                }
            })
        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }


    }


    fun generateLineChart(
        lineChart: LineChart,
        averageTempo: Double,
        averageInstrumentalness: Double,
        averageAcousticness: Double,
        averageEnergy: Double
    ) {
        // Create a list of Entry with the average values.
        val lineEntries = mutableListOf(
            Entry(0f, averageTempo.toFloat() /2),
            Entry(1f, averageInstrumentalness.toFloat()/3),
            Entry(2f, averageAcousticness.toFloat()/2),
            Entry(3f, averageEnergy.toFloat()/4),
            Entry(4f, averageTempo.toFloat()/2),
            Entry(5f, averageInstrumentalness.toFloat()/3),
            Entry(6f, averageAcousticness.toFloat()/2),
            Entry(7f, averageEnergy.toFloat()/4),
            Entry(8f, averageTempo.toFloat()/2),
            Entry(9f, averageInstrumentalness.toFloat()/3),
            Entry(10f, averageAcousticness.toFloat()/2),
            Entry(11f, averageEnergy.toFloat()/4),
            Entry(12f, averageTempo.toFloat()/2),
            Entry(13f, averageInstrumentalness.toFloat()/3),
            Entry(14f, averageAcousticness.toFloat()/2),
            Entry(15f, averageEnergy.toFloat()/4),



        )


        // Create a LineDataSet with lineEntries and set label.
        val dataSet = LineDataSet(lineEntries, "").apply {
            //color = Color.parseColor("#FFBB86FC") // Replace with your desired color code

            valueTextColor = Color.WHITE
            lineWidth = 3f
            circleRadius = 5f
            setCircleColor(Color.parseColor("#FF6200EE")) // Replace with your desired color code
            setDrawValues(false)
            setDrawCircles(false)
            setDrawCircleHole(false)
            setDrawFilled(true)
            fillColor = Color.parseColor("#FF6200EE") // Replace with your desired color code
            mode = LineDataSet.Mode.CUBIC_BEZIER // Set mode to CUBIC_BEZIER for smooth curves
            cubicIntensity = 0.2f // Adjust the intensity for the curve smoothness
        }


        val data = LineData(dataSet)

        // Set data to the line chart.
        lineChart.data = data

        // Customize the line chart further as needed.
        val xAxis = lineChart.xAxis
        xAxis.setDrawLabels(false) // No labels for X-axis
        xAxis.setDrawGridLines(false) // No grid lines
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        lineChart.axisLeft.apply {
            setDrawLabels(true) // Adjust as needed
            setDrawGridLines(true)
            // Adjust these values as needed to "zoom out" the wave
            axisMinimum = 0f // This should be the minimum value you expect for your data
            axisMaximum = 100f // This should be the maximum value you expect for your data
        }

        // Customize the numbers on the horizontal part of the chart.
        lineChart.axisLeft.setDrawLabels(false) // No labels for left Y-axis
        lineChart.axisRight.isEnabled = false // Disable right Y-axis

        // Formatter for displaying values (optional)
        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "%.2f".format(value)
            }
        })

        // Other customizations
        lineChart.setDrawGridBackground(false)
        lineChart.setDrawBorders(false)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false // Disable the legend
        //lineChart.animateXY(1500, 1500, Easing.EaseInExpo)

        lineChart.invalidate() // Refresh the line chart
        startWaveAnimation(lineChart, dataSet,0.3f)


    }


    private fun startWaveAnimation(lineChart: LineChart, dataSet: LineDataSet, amplitude: Float) {
        job?.cancel() // Cancel any existing job
        job = CoroutineScope(Dispatchers.Main).launch {
            var phaseShift = 10f // Initial phase shift

            while (isActive) {
                // Calculate a new phase shift for each x-value
                val phaseShifts = FloatArray(dataSet.entryCount) { index ->
                    (phaseShift + index * 10f) * 0.5f // Adjust the wave speed as needed (0.1f in this example)
                }

                // Update the y-values of each entry to create a moving wave
                dataSet.values.forEachIndexed { index, entry ->
                    val originalY = lineChart.data.getDataSetByIndex(0).getEntryForXValue(entry.x, 0f).y
                    val yOffset = originalY + amplitude * kotlin.math.sin(phaseShifts[index])
                    entry.y = yOffset
                }

                // Increment the phase shift
                phaseShift += 0.1f // Adjust the phase shift increment as needed

                dataSet.notifyDataSetChanged()
                lineChart.notifyDataSetChanged()
                lineChart.invalidate()

                delay(25) // Control the speed of the wave animation
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    private fun captureChartAsBitmap(lineChart: LineChart, width: Int, height: Int): Bitmap {
        // Create a bitmap with the desired width and height.
        val chartBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Create a canvas with the bitmap.
        val canvas = Canvas(chartBitmap)

        // Set the chart's dimensions to match the bitmap's size.
        lineChart.layout(0, 0, width, height)

        // Draw the chart onto the canvas.
        lineChart.draw(canvas)

        return chartBitmap
    }

    fun shareImage(context: Context, lineChart: LineChart) {
        lineChart.invalidate()

        // Wait for the chart to be fully drawn before capturing the bitmap.
        lineChart.post {
            val chartWidth = 900 // Width in pixels
            val chartHeight = 600 // Height in pixels
            val chartBitmap = captureChartAsBitmap(lineChart, chartWidth, chartHeight)

            // Create an intent to share the captured chart.
            val intent = Intent(Intent.ACTION_SEND)
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, chartBitmap, "Line Graph", null)
            val uri = Uri.parse(path)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.type = "image/*"
            context.startActivity(Intent.createChooser(intent, "Share To: "))
        }
    }




    fun setRecyclerViewForArtists(itemList: List<RequestDataInterface.MyPlaylistsResponse>){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.artists_rec_view)
        if(itemList.isEmpty()){
            recyclerView?.visibility =  View.INVISIBLE
        }
        else{
            recyclerView?.layoutManager =  GridLayoutManager(ctx, 1, GridLayoutManager.HORIZONTAL, false)

            val adapter = RecyclerViewAdapter(itemList, 2)
            recyclerView?.adapter = adapter
            recyclerView?.visibility =  View.VISIBLE
        }
    }

    fun setRecyclerViewForTracks(itemList: List<RequestDataInterface.MyPlaylistsResponse>){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.tracks_rec_view)
        if(itemList.isEmpty()){
            recyclerView?.visibility =  View.INVISIBLE
        }
        else{
            recyclerView?.layoutManager =  GridLayoutManager(ctx, 1, GridLayoutManager.HORIZONTAL, false)

            val adapter = RecyclerViewAdapter(itemList, 3)
            recyclerView?.adapter = adapter
            recyclerView?.visibility =  View.VISIBLE
        }
    }

    fun switchToImportTrack(){
        val i = Intent(ctx, ImportTrack::class.java)
        ctx.startActivity(i)
    }

    fun switchToSeeAllFriends(){
        val i = Intent(ctx, SeeAllFriends::class.java)
        ctx.startActivity(i)
        (ctx as? Activity)?.finish() // Finish the current activity
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
                            //Log.e("array size: ", response.body()!!.friends.size.toString())
                            //Log.e("array size: ", response.body()!!.friends[0].username.toString())
                            //Log.e("array size: ", response.body()!!.friends[1].username.toString())
                            if (response.body()!!.friends.size == 1){ // eğer sadece 1 arkadaşı varsa
                                val friend1 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername1)
                                friend1!!.text = response.body()!!.friends[0].username
                            }
                            else{
                                if (response.body()!!.friends.size == 0){
                                    val friend1 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername1)
                                    friend1!!.text = "No friend to display"
                                    val remove1 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileVibeFriend1)
                                    val vibe1 = (ctx as? Activity)?.findViewById<TextView>(R.id.realVibeFriend1)
                                    remove1!!.visibility = View.INVISIBLE
                                    vibe1!!.visibility = View.INVISIBLE
                                }

                            }
                            if (response.body()!!.friends.size == 2){ // eğer iki arkadaşı varsa
                                val friend1 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername1)
                                friend1!!.text = response.body()!!.friends[0].username
                                val friend2 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername2)
                                friend2!!.text = response.body()!!.friends[1].username

                            }
                            else{
                                val friend2 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername2)
                                friend2!!.text ="No friend to display"
                                val remove2 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileVibeFriend2)
                                val vibe2 = (ctx as? Activity)?.findViewById<TextView>(R.id.realVibeFriend2)
                                remove2!!.visibility = View.INVISIBLE
                                vibe2!!.visibility = View.INVISIBLE
                            }
                            if (response.body()!!.friends.size == 3){
                                val friend1 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername1)
                                friend1!!.text = response.body()!!.friends[0].username
                                val friend2 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername2)
                                friend2!!.text = response.body()!!.friends[1].username
                                val friend3 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername3)
                                friend3!!.text = response.body()!!.friends[2].username
                            }

                            else{
                                val friend3 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername3)
                                friend3!!.text = "No friend to display"
                                val remove3 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileVibeFriend3)
                                val vibe3 = (ctx as? Activity)?.findViewById<TextView>(R.id.realVibeFriend3)
                                remove3!!.visibility = View.INVISIBLE
                                vibe3!!.visibility = View.INVISIBLE
                            }
                            if (response.body()!!.friends.size == 4){
                                val friend1 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername1)
                                friend1!!.text = response.body()!!.friends[0].username
                                val friend2 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername2)
                                friend2!!.text = response.body()!!.friends[1].username
                                val friend3 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername3)
                                friend3!!.text = response.body()!!.friends[2].username
                                val friend4 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername4)
                                friend4!!.text = response.body()!!.friends[3].username
                            }
                            else{
                                val friend4 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileFriendUsername4)
                                friend4!!.text ="No friend to display"
                                val remove4 = (ctx as? Activity)?.findViewById<TextView>(R.id.profileVibeFriend4)
                                val vibe4 = (ctx as? Activity)?.findViewById<TextView>(R.id.realVibeFriend4)
                                remove4!!.visibility = View.INVISIBLE
                                vibe4!!.visibility = View.INVISIBLE
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

    fun getFriends(){
        try{
            val retrofitBuilder = retrofitClient.createAPIRequest()
            val token = "Bearer " + constants.bearerToken
            Log.e("bearer set username", constants.bearerToken)
            val retrofitData = retrofitBuilder.userGetData(token)
            Log.e("getFriends process", "Going...")

            retrofitData.enqueue(object : Callback<RequestDataInterface.getUserDataResponse> {
                override fun onResponse(call: Call<RequestDataInterface.getUserDataResponse>, response: Response<RequestDataInterface.getUserDataResponse>) {
                    Log.e("get friends response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body().toString()
                        //val friendsList = response.body()!!.friends ?: emptyList()
                        val holder = response.body()!!.friends
                        for (friends in holder){
                            constants.friendsList.add(friends)
                            Log.e("constants", constants.friendsList.toString())
                        }
                        Log.e("getFriends response: ", responseBody ?: "Response body is null")
                        // Parse the responseBody as needed or handle the string response



                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("getFriends error: ", "HTTP ${response.code()}: $errorBody")


                        } catch (e: Exception) {
                            Log.e("getFriends error: ", "Error parsing error response.")
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
        getFriends()
        currFriends = constants.friendsList
        Log.e("friends in remove:", currFriends.toString())
        for (friend in currFriends){
            Log.e("friends in remove:", currFriends.toString())
            Log.e("username param:", username)
            Log.e("data coming:", friend.username)
            if (friend.username == username){
                val friendId = friend.id
                Log.e("id ", friendId)
                try{
                    val retrofitBuilder = retrofitClient.createAPIRequest()
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
                                setUsernameandFriends()
                                Toast.makeText(ctx, "Friend '${username}' successfully removed !", Toast.LENGTH_LONG).show()
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


    fun refreshArtists(){
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)

            val token = "Bearer " + constants.bearerToken
            val retrofitData = retrofitBuilder.recommendTrack(token)

            retrofitData.enqueue(object : Callback<List<RequestDataInterface.RecommendTrackResponse>> {
                override fun onResponse(call: Call<List<RequestDataInterface.RecommendTrackResponse>>, response: Response<List<RequestDataInterface.RecommendTrackResponse>>) {
                    Log.e("artist response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("artist response: ", (responseBody ?: "Response body is null").toString())
                        if (responseBody!!.isNotEmpty()){
                            val trackNames = responseBody?.get(0)?.ratedTracks
                            val itemList = mutableListOf<RequestDataInterface.MyPlaylistsResponse>()
                            val itemList2 = mutableListOf<RequestDataInterface.MyPlaylistsResponse>()
                            for(track in trackNames!!){
                                val response = RequestDataInterface.MyPlaylistsResponse(track.id, track.artists[0])
                                val response2 = RequestDataInterface.MyPlaylistsResponse(track.id, track.name)
                                if(!itemList.contains(response)){
                                    itemList.add(response)
                                }
                                if(!itemList2.contains(response2)){
                                    itemList2.add(response2)
                                }
                            }
                            setRecyclerViewForArtists(itemList)
                            setRecyclerViewForTracks(itemList2)
                        }

                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("artist error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Log.e("artist error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<List<RequestDataInterface.RecommendTrackResponse>>, t: Throwable) {
                    Log.e("artist error: ", t.toString())
                }
            })

        }
        catch (e: Exception) {
            Log.e("artist", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }


}