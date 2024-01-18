package view

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nitish.typewriterview.TypeWriterView
import com.squareup.picasso.Picasso
import viewmodel.DashboardViewModel


class my_room : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var viewModel: DashboardViewModel
    private val maxProgress = 450 // you can set it as you want
    private val minVolumeLevel = 0.1f // Adjust this value as needed (0.0 to 1.0)
    private val maxVolumeLevel = 35.0f // Adjust this value as needed (0.0 to 1.0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_room)

        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        viewModel.setContext(this)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        val fireSeekBar : SeekBar = findViewById(R.id.slider_fireplace)
        fireSeekBar.max = maxProgress
        var mediaPlayer: MediaPlayer? = null

        fireSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Calculate the volume level based on progress
                val volumeLevel =
                    minVolumeLevel + (1.0f - minVolumeLevel) * (progress.toFloat() / maxProgress)
                mediaPlayer?.setVolume(volumeLevel, volumeLevel)
                mediaPlayer?.setVolume(volumeLevel, volumeLevel)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Nothing to do here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Nothing to do here
            }
        })

        val firePlay : FloatingActionButton = findViewById(R.id.firePlaceBtn)
        firePlay.setOnClickListener {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, R.raw.fireplace)

            // Set the initial volume based on the SeekBar progress
            val initialVolumeLevel = minVolumeLevel + (maxVolumeLevel - minVolumeLevel) * (fireSeekBar.progress.toFloat() / maxProgress)
            mediaPlayer?.setVolume(initialVolumeLevel, initialVolumeLevel)


            mediaPlayer?.start()
        }


        /*
        viewModel.setRecyclerView()
        viewModel.setRecyclerViewForArtists()
        viewModel.setRecyclerViewForTracks()

         */
        val personalRoomTrack : TypeWriterView = findViewById(R.id.personalRoomTrack)
        //personalRoomTrack.setCharacterDelay(150)
       //personalRoomTrack.animateText("After Hours")
        //personalRoomTrack.animate()


        val roomTrack = intent.getStringExtra("trackName")
        val keyword = intent.getStringExtra("keyword")
        if(keyword == "addRoomTrack"){
            personalRoomTrack.setCharacterDelay(150)
            personalRoomTrack.animateText(roomTrack)
        }
        else{
            viewModel.setRoomSong()
        }
        val addRoomSongBtn : Button = findViewById(R.id.addRoomSong)
        if (personalRoomTrack.text.isEmpty()){
            addRoomSongBtn.visibility = View.GONE
        }

        addRoomSongBtn.setOnClickListener {
            startActivity(Intent(this, AddRoomTrack::class.java))

        }
        val roomFrame : ImageView = findViewById(R.id.roomFrame)
        //roomFrame.setImageResource(R.drawable.default_room)
        Picasso.with(this)
            .load(R.drawable.default_room) // Replace with your image resource or URL
            .fit()
            .into(roomFrame)

        val settingBtn : TextView = findViewById(R.id.nav_settings)
        settingBtn.setOnClickListener {
            viewModel.switchToSettings()
        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }

    fun goToDashboard(view: View) {
        startActivity(Intent(this, Dashboard::class.java))
    }

    fun goToProfile(view: View){
        startActivity(Intent(this, ProfilePage::class.java))
    }
}