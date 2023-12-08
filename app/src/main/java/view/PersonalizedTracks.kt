package view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import network.constants
import viewmodel.PersonalizedTracksViewModel


class PersonalizedTracks : AppCompatActivity() {
    private lateinit var viewModel: PersonalizedTracksViewModel
    lateinit var barChart: BarChart

    lateinit var barData: BarData
    var criteria = arrayOf("Accousticness", "Energy", "Instrumentalness", "Mood")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalized_tracks)
        barChart = findViewById(R.id.barChart);

        viewModel = ViewModelProvider(this).get(PersonalizedTracksViewModel::class.java)
        viewModel.setContext(this)

        val spinner1 = findViewById<Spinner>(R.id.acousticSpinner)
        val categoriesList = arrayOf<String?>("Item1","Item2", "Item3","Item4")
        val adapter1 =
            ArrayAdapter<Any?>(this, com.example.cs308_00.R.layout.customized_spinner_item, categoriesList)
        adapter1.setDropDownViewResource(R.layout.customized_spinner_list)
        spinner1.adapter = adapter1

        val spinner2  = findViewById<Spinner>(R.id.moodSpinner)
        val adapter2  = ArrayAdapter<Any?>(this, com.example.cs308_00.R.layout.customized_spinner_item, categoriesList)
        adapter2.setDropDownViewResource(R.layout.customized_spinner_list)
        spinner2.adapter = adapter2

        val spinner3  = findViewById<Spinner>(R.id.instrumentsSpinner)
        val adapter3  = ArrayAdapter<Any?>(this, com.example.cs308_00.R.layout.customized_spinner_item, categoriesList)
        adapter3.setDropDownViewResource(R.layout.customized_spinner_list)
        spinner3.adapter = adapter3


        val spinner4  = findViewById<Spinner>(R.id.energySpinner)
        val adapter4  = ArrayAdapter<Any?>(this, com.example.cs308_00.R.layout.customized_spinner_item, categoriesList)
        adapter4.setDropDownViewResource(R.layout.customized_spinner_list)
        spinner4.adapter = adapter4




        viewModel.getOwnTracks { accousticAvg, instrumAvg, tempoAvg, energyAvg ->
            // This code will execute once the averages are available
            // You can use the averages here in your activity
            Log.e("Acoustic Average :", accousticAvg.toString())
            Log.e("Instrum Average:", instrumAvg.toString())
            Log.e("Tempo Average:", tempoAvg.toString())
            Log.e("Energy Average:", energyAvg.toString())


            // Update your UI or perform other actions based on the averages
        }






        // Generate bar chart data dynamically.

        //barData = generateBarChartData(viewModel.accousticAv, viewModel.instrumAv, viewModel.tempoAv, viewModel.energyAv)

        // Set up the bar chart.
        //setupBarChart()




    }

    private fun generateBarChartData(
        accousticAv: Int,
        instrumAv: Int,
        tempoAv: Int,
        energyAv: Int
    ): BarData {
        val barEntries = mutableListOf<BarEntry>()

        val criteria = arrayOf("Accousticness", "Energy", "Instrumentalness", "Mood")

        // Add the average values to the bar entries.
        barEntries.add(BarEntry(1f, accousticAv.toFloat()))
        barEntries.add(BarEntry(2f, energyAv.toFloat()))
        barEntries.add(BarEntry(3f, instrumAv.toFloat()))
        barEntries.add(BarEntry(4f, tempoAv.toFloat()))

        val dataSet = BarDataSet(barEntries, "Averages")

        return BarData(dataSet)
    }





    private fun setupBarChart() {
        // Set bar chart data.
        barChart.data = barData

        // Customize the appearance of the bar chart as needed.
        barChart.description.isEnabled = false
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(criteria)
        xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setGranularity(1f)
        xAxis.setGranularityEnabled(true)
        barChart.setDragEnabled(true)
        barChart.setVisibleXRangeMaximum(3f)
        val barSpace = 0.1f
       // val groupSpace = 0.5f
        barData.barWidth = 0.15f
        barChart.xAxis.axisMinimum = 0f
        barChart.animate()
        //barChart.groupBars(0f, groupSpace, barSpace)
        barChart.invalidate()
    }

    fun goToProfilePage(view :View){
        startActivity(Intent(this, ProfilePage::class.java))
    }



}