package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R

import viewmodel.SearchPlaylistViewModel

class SearchPlaylist : AppCompatActivity() {
    private lateinit var viewModel: SearchPlaylistViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_playlist)

        viewModel = ViewModelProvider(this).get(SearchPlaylistViewModel::class.java)
        viewModel.setContext(this)
        val searchBtn : ImageButton = findViewById(R.id.searchButton)
        val searchTxt : EditText = findViewById(R.id.playlistTxt)

        searchBtn.setOnClickListener {
            if(searchTxt.text.isEmpty()){
                Toast.makeText(this, "Please enter a playlist name", Toast.LENGTH_LONG).show()
            }
            else{
                viewModel.searchPlaylist(searchTxt.text.toString())
            }
        }

        val goBack : ImageButton = findViewById(R.id.backArrow)
        goBack.setOnClickListener {
            viewModel.switchToDashboard()
        }

    }
}