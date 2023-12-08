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
import model.RequestDataInterface
import viewmodel.ImportTrackViewModel

class ImportTrack : AppCompatActivity() {

    private lateinit var viewModel: ImportTrackViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_track)
        viewModel = ViewModelProvider(this).get(ImportTrackViewModel::class.java)
        viewModel.setContext(this)



        val backToProfile = findViewById<ImageButton>(R.id.goBackBtn)
        backToProfile.setOnClickListener {
            super.onBackPressed()
        }
        val importBtn = findViewById<Button>(R.id.importButton)




        importBtn.setOnClickListener {
            val albumType : String = findViewById<EditText>(R.id.albumType).text.toString()
            val artistName: String = findViewById<EditText>(R.id.artistInput).text.toString()
            val tempo : String = findViewById<EditText>(R.id.tempoInput).text.toString()
            val tempoInt = tempo.toIntOrNull()!!
            val instrument : String = findViewById<EditText>(R.id.instrumentalInput).text.toString()
            val instrumentInt = instrument.toIntOrNull()!!
            val accoustic : String = findViewById<EditText>(R.id.accousticInput).text.toString()
            val accousticInt = accoustic.toIntOrNull()!!
            val energy : String = findViewById<EditText>(R.id.energyInput).text.toString()
            val energyInt = energy.toIntOrNull()!!
            val albumName : String = findViewById<EditText>(R.id.albumInput).text.toString()
            val releaseYear : String = findViewById<EditText>(R.id.releaseYear).text.toString()
            val releaseYearInt: Int = releaseYear.toIntOrNull()!!
            val totalTracks : String = findViewById<EditText>(R.id.totalTracks).text.toString()
            val totalTracksInt: Int = totalTracks.toIntOrNull()!!
            val trackName : String = findViewById<EditText>(R.id.trackInput).text.toString()
            val duration : String = findViewById<EditText>(R.id.durationInput).text.toString()
            val durationInt = duration.toIntOrNull()!!
            val albumGenre : String = findViewById<EditText>(R.id.albumGenre).text.toString()
            val trackGenre : String = findViewById<EditText>(R.id.trackGenre).text.toString()
            val artistGenre : String = findViewById<EditText>(R.id.artistGenre).text.toString()

            val genresInput = artistGenre.trim() // Get the input text and remove leading/trailing spaces
            val artistGenreList = genresInput.split(" ") // Split the input text into a list of strings

            val albumgenresInput = albumGenre.trim() // Get the input text and remove leading/trailing spaces
            val albumGenreList = genresInput.split(" ") // Split the input text into a list of strings

            val artistData = RequestDataInterface.Artist(artistGenreList, artistName)
            val artistList = listOf<RequestDataInterface.Artist>(artistData)
            val albumData = RequestDataInterface.Album(albumType, artistList , albumGenreList,albumName, releaseYearInt ,totalTracksInt)
            val importData = RequestDataInterface.TrackData(albumData, artistList, trackName, trackGenre, durationInt, tempoInt, instrumentInt, accousticInt, energyInt)
            val request = RequestDataInterface.addTrackRequest(importData)
            viewModel.addTrack(request)


        }


    }

}