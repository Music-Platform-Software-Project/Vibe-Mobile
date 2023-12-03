package view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface
import viewmodel.DashboardViewModel
import viewmodel.DetailedPlaylistViewModel
import viewmodel.SearchPlaylistRecViewAdapter

class DetailedPlaylist : AppCompatActivity() {
    private lateinit var viewModel: DetailedPlaylistViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_playlist)
        viewModel = ViewModelProvider(this).get(DetailedPlaylistViewModel::class.java)
        viewModel.setContext(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val id = intent.getStringExtra("id")

        viewModel.getDetails(id!!)

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