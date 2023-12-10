package view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface
import network.constants
import viewmodel.DashboardViewModel
import viewmodel.DetailedPlaylistViewModel
import viewmodel.SearchPlaylistRecViewAdapter

class DetailedPlaylist : AppCompatActivity() {
    private lateinit var viewModel: DetailedPlaylistViewModel
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_playlist)
        viewModel = ViewModelProvider(this).get(DetailedPlaylistViewModel::class.java)
        viewModel.setContext(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val id = intent.getStringExtra("id")
        val from = intent.getStringExtra("from")
        val image = findViewById<ImageView>(R.id.playlistIcon)
        image.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
        }
        textView = findViewById(R.id.playlistName)
        viewModel.getDetails(id!!)

        val editButton: Button = findViewById(R.id.editPlaylist)
        val deleteButton: Button = findViewById(R.id.deletePlaylist)
        val addTrack: LinearLayout = findViewById(R.id.addToPlaylist)
        val changeName: ImageView = findViewById(R.id.changePlaylistName)
        if (from == "search"){
            deleteButton.isClickable = false
            deleteButton.setBackgroundResource(R.drawable.unclikcable_button_bcg)
            editButton.isClickable = false
            editButton.setBackgroundResource(R.drawable.unclikcable_button_bcg)
        }
        else{
            deleteButton.setOnClickListener {
                showInputDialog(id)
            }
            editButton.setOnClickListener{
                //TODO
            }
            addTrack.setOnClickListener {
                val intent = Intent(this, SearchTrack::class.java)
                intent.putExtra("addToPlaylist", true)
                startActivity(intent)
            }
            changeName.setOnClickListener {
                showInputDialog2(id)
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

    private fun showInputDialog(id: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure?")

        val input = EditText(this)
        builder.setView(input)
        input.hint = "Type SURE if you are certain"

        builder.setPositiveButton("OK") { _, _ ->
            val userInput = input.text.toString()
            if(userInput == "SURE"){
                viewModel.deletePlaylist(id)
            }
            // Handle the user input here
            // For example, you can display it in a Toast
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showInputDialog2(playlistID: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Rename Your Liked Songs Collection")

        val input = EditText(this)
        builder.setView(input)
        input.hint = "Name of the Liked Songs Collection"

        builder.setPositiveButton("OK") { _, _ ->
            val userInput = input.text.toString()

            viewModel.changePlaylistName(playlistID,userInput)

            // Handle the user input here
            // For example, you can display it in a Toast
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}