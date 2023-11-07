package view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.PersonalizedTracksViewModel

class PersonalizedTracks : AppCompatActivity() {
    private lateinit var viewModel: PersonalizedTracksViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalized_tracks)

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

    }

    fun goToProfilePage(view :View){
        startActivity(Intent(this, ProfilePage::class.java))
    }



}