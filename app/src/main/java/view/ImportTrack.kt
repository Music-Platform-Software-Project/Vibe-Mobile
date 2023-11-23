package view

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.ImportTrackViewModel

class ImportTrack : AppCompatActivity() {

    private lateinit var viewModel: ImportTrackViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_track)
        viewModel = ViewModelProvider(this).get(ImportTrackViewModel::class.java)
        viewModel.setContext(this)
        val genreSpinner = findViewById<Spinner>(R.id.genreSpinner)
        val moodSpinner = findViewById<Spinner>(R.id.moodSpinner)
        val moodList = arrayOf<String?>("High", "Medium", "Low")
        val genreList = arrayOf<String?>(
            "Ambient",
            "Blues",
            "Classical",
            "Country",
            "Disco",
            "Electronic",
            "Folk",
            "Funk",
            "Gospel",
            "Hip Hop",
            "House",
            "Indie",
            "Jazz",
            "K-Pop",
            "Latin",
            "Metal",
            "Pop",
            "Punk",
            "R&B",
            "Reggae",
            "Rock",
            "Ska",
            "Soul",
            "Trance"
        )


        val genreAdapter =
            ArrayAdapter<Any?>(this, R.layout.customized_spinner_item, genreList)
        genreAdapter.setDropDownViewResource(R.layout.customized_spinner_list)
        genreSpinner.adapter = genreAdapter

        val moodAdapter =
            ArrayAdapter<Any?>(this, R.layout.customized_spinner_item, moodList)
        moodAdapter.setDropDownViewResource(R.layout.customized_spinner_list)
        moodSpinner.adapter = moodAdapter

        val backToProfile = findViewById<ImageButton>(R.id.goBackBtn)
        backToProfile.setOnClickListener {
            viewModel.backToProfile()
        }
        val importBtn = findViewById<Button>(R.id.importButton)




        importBtn.setOnClickListener {
            val selectedGenre: String = genreSpinner.selectedItem.toString()
            Log.e("selected genre", selectedGenre)
            val selectedMood: String = moodSpinner.selectedItem.toString()
            Log.e("selected mood", selectedMood)
            val tempoInput : Int = findViewById<EditText>(R.id.tempoInput).text.toString().toInt()
            val artistInput = findViewById<EditText>(R.id.artistInput).text.toString()
            val artistsList = artistInput.split("\\s+").map { it.trim() }
            Log.e("artist list", artistsList.toString())
            val trackInput = findViewById<EditText>(R.id.trackInput).text.toString()
            val albumInput = findViewById<EditText>(R.id.albumInput).text.toString()
            val accousticInput: Int = findViewById<EditText>(R.id.accousticInput).text.toString().toInt()
            val energyInput: Int = findViewById<EditText>(R.id.energyInput).text.toString().toInt()
            val instrumentalInput : Int = findViewById<EditText>(R.id.instrumentalInput).text.toString().toInt()
            viewModel.addTrack(trackInput,artistsList, albumInput, selectedGenre,tempoInput, accousticInput,  energyInput, instrumentalInput, selectedMood )
        }


    }

}