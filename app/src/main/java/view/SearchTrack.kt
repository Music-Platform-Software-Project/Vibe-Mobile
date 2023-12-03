package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.LoginPageViewModel
import viewmodel.SearchTrackViewModel

class SearchTrack : AppCompatActivity() {
    private lateinit var viewModel: SearchTrackViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_track)

        viewModel = ViewModelProvider(this).get(SearchTrackViewModel::class.java)
        viewModel.setContext(this)
        val searchButton : ImageButton = findViewById(R.id.searchButton)
        val searchTxt : EditText = findViewById(R.id.trackTxt)

        searchButton.setOnClickListener {
            if(searchTxt.text.isEmpty()){
                Toast.makeText(this, "Please enter a track name", Toast.LENGTH_LONG).show()
            }
            else{
                viewModel.searchTrack(searchTxt.text.toString())
            }
        }

        val goBack : ImageButton = findViewById(R.id.backArrow)
        goBack.setOnClickListener {
            viewModel.switchToDashboard()
        }

    }
}