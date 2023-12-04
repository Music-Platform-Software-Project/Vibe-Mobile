package view


import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import network.constants
import viewmodel.DetailedTrackViewModel

class DetailedTrack : AppCompatActivity() {
    private lateinit var viewModel: DetailedTrackViewModel
    private lateinit var idx : String
    private lateinit var fromWhere : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_track)

        viewModel = ViewModelProvider(this).get(DetailedTrackViewModel::class.java)
        viewModel.setContext(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val id = intent.getStringExtra("id")
        val from = intent.getStringExtra("from")
        idx = id!!
        fromWhere = from!!
        viewModel.getAll(id!!)

        val rBar = findViewById<RatingBar>(R.id.rBar)
        val msg = rBar.rating

        val rateButton = findViewById<Button>(R.id.trackRate)
        rateButton.setOnClickListener {
            //viewModel.rateTrack(trackID, myRate)
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

    fun deleteTrack(view: View) {
        showInputDialog(fromWhere)
    }

    private fun showInputDialog(fromWhere: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure?")

        val input = EditText(this)
        builder.setView(input)
        input.hint = "Type SURE if you are certain"

        builder.setPositiveButton("OK") { _, _ ->
            val userInput = input.text.toString()
            if(userInput == "SURE"){
                if(fromWhere == "playlist"){
                    viewModel.deleteTrack2(idx, constants.currentPlaylistID)
                }
                else{
                    viewModel.deleteTrack(idx)
                }

            }
            // Handle the user input here
            // For example, you can display it in a Toast
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

}