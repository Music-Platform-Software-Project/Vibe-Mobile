package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.DashboardViewModel
import viewmodel.ImportTrackSpotifyViewModel
import viewmodel.ImportTrackViewModel

class ImportTrackSpotify : AppCompatActivity() {
    private lateinit var viewModel: ImportTrackSpotifyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_track_spotify)
        viewModel = ViewModelProvider(this).get(ImportTrackSpotifyViewModel::class.java)
        viewModel.setContext(this)

        val backBtn : ImageButton = findViewById(R.id.goBackBtn)
        backBtn.setOnClickListener {
            viewModel.backToDash()
        }

        val import : Button = findViewById(R.id.importButton)
        import.setOnClickListener {
            val url : String = findViewById<EditText>(R.id.spotiUrl).text.toString()
            if (url.isNullOrEmpty()){
                Toast.makeText(this, "Please enter a URL to import track ", Toast.LENGTH_LONG).show()
            }
            else{
                viewModel.importSpotify(url)
            }

        }
    }
}