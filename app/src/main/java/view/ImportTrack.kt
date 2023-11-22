package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.ImportTrackViewModel
import viewmodel.PersonalizedTracksViewModel

class ImportTrack : AppCompatActivity() {

    private lateinit var viewModel: ImportTrackViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_track)
        viewModel = ViewModelProvider(this).get(ImportTrackViewModel::class.java)
        viewModel.setContext(this)
        val genreSpinner =  findViewById<Spinner>(R.id.genreSpinner)
        val moodSpinner = findViewById<Spinner>(R.id.moodSpinner)
        val moodList =  arrayOf<String?>("High","Medium", "Low")
        val genreList = arrayOf<String?>("Ambient","Blues", "Classical","Country","Disco","Electronic",
            "Folk","Funk","Gospel","Hip Hop", "House","Indie","Jazz","K-Pop", "Latin","Metal",
            "Pop","Punk","R&B","Reggae", "Rock","Ska","Soul","Trance")



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

    }
}