package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.AddRoomTrackViewModel

class AddRoomTrack : AppCompatActivity() {

    private lateinit var viewModel : AddRoomTrackViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_room_track)
        viewModel = ViewModelProvider(this).get(AddRoomTrackViewModel::class.java)
        viewModel.setContext(this)
        val searchTxt : EditText = findViewById(R.id.trackTxt)
        val searchButton : ImageButton = findViewById(R.id.searchButton)

        searchButton.setOnClickListener {
            if(searchTxt.text.isEmpty()){
                Toast.makeText(this, "Please enter a track name", Toast.LENGTH_LONG).show()
            }
            else{

                viewModel.searchTrack(searchTxt.text.toString())

            }
        }
    }
}