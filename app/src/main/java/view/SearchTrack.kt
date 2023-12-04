package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
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
}