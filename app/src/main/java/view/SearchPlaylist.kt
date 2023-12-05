package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        searchBtn.setOnClickListener {
            if(searchTxt.text.isEmpty()){
                Toast.makeText(this, "Please enter a playlist name", Toast.LENGTH_LONG).show()
            }
            else{
                viewModel.searchPlaylist(searchTxt.text.toString())
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