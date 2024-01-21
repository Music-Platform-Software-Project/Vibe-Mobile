package view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.LoginPageViewModel
import viewmodel.SearchTrackViewModel
import java.util.Locale
import java.util.Objects

class SearchTrack : AppCompatActivity() {
    private lateinit var viewModel: SearchTrackViewModel
    private var REQUEST_CODE_SPEECH_INPUT = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_track)

        viewModel = ViewModelProvider(this).get(SearchTrackViewModel::class.java)
        viewModel.setContext(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val searchButton : ImageButton = findViewById(R.id.searchButton)
        val searchTxt : EditText = findViewById(R.id.trackTxt)

        val voiceBtn : ImageButton = findViewById(R.id.getVoiceBtn)
        voiceBtn.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Text")

            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            }catch (e : Exception){
                Toast.makeText(this, " exception" + e.message, Toast.LENGTH_LONG).show()
            }
        }
        searchButton.setOnClickListener {
            if(searchTxt.text.isEmpty()){
                Toast.makeText(this, "Please enter a track name", Toast.LENGTH_LONG).show()
            }
            else{
                val addToPlaylist = intent.getBooleanExtra("addToPlaylist", false)
                if (addToPlaylist){
                    viewModel.searchTrackToAdd(searchTxt.text.toString())

                }
                else{
                    viewModel.searchTrack(searchTxt.text.toString())
                }

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("request code ", requestCode.toString())
        Log.e("speech input ", REQUEST_CODE_SPEECH_INPUT.toString())
        Log.e("data  ", data.toString())
        if (requestCode == REQUEST_CODE_SPEECH_INPUT){
            Log.e("okkk ", RESULT_OK.toString())
            if(requestCode == 1 && data != null){
                val res : ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                Log.e("resultsss ", res.toString())
                val inputField : EditText = findViewById<EditText?>(R.id.trackTxt)
                Log.e("voice info", Objects.requireNonNull(res)[0])
                if (res.isNotEmpty()) {
                    val voiceText = res[0]
                    val capitalizedText = voiceText.substring(0, 1).toUpperCase() + voiceText.substring(1)
                    inputField.setText(capitalizedText)
                }
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