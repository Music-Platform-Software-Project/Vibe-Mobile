package view

import android.app.AlertDialog
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nitish.typewriterview.TypeWriterView
import com.squareup.picasso.Picasso
import network.constants
import org.w3c.dom.Text
import viewmodel.DashboardViewModel


class my_room : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var viewModel: DashboardViewModel
    private val maxProgress = 450 // you can set it as you want
    private val minVolumeLevel = 0.1f // Adjust this value as needed (0.0 to 1.0)
    private val maxVolumeLevel = 35.0f // Adjust this value as needed (0.0 to 1.0)
    private var mediaPlayerCafe: MediaPlayer? = null
    private var mediaPlayerRain: MediaPlayer? = null
    private var mediaPlayerNature: MediaPlayer? = null
    private var mediaPlayerFire: MediaPlayer? = null


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


        val cafeStop : FloatingActionButton = findViewById(R.id.cafeStopBtn)
        val fireStop : FloatingActionButton = findViewById(R.id.firePlaceStopBtn)
        val rainStop : FloatingActionButton = findViewById(R.id.rainStopBtn)
        val natureStop : FloatingActionButton = findViewById(R.id.natureStopBtn)
        val cafeSeekBar : SeekBar = findViewById(R.id.slider_cafe)
        val rainSeekBar : SeekBar = findViewById(R.id.slider_rain)
        val natureSeekBar : SeekBar = findViewById(R.id.slider_nature)
        val fireSeekBar : SeekBar = findViewById(R.id.slider_fireplace)
        val changeImage : TextView = findViewById(R.id.changeImage)
        fireSeekBar.max = maxProgress
        cafeSeekBar.max = maxProgress
        rainSeekBar.max = maxProgress
        natureSeekBar.max = maxProgress
        var mediaPlayer: MediaPlayer? = null

        fireSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Calculate the volume level based on progress
                val volumeLevel =
                    minVolumeLevel + (1.0f - minVolumeLevel) * (progress.toFloat() / maxProgress)
                mediaPlayerFire?.setVolume(volumeLevel, volumeLevel)
                mediaPlayerFire?.setVolume(volumeLevel, volumeLevel)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Nothing to do here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Nothing to do here
            }
        })

        cafeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Calculate the volume level based on progress
                val volumeLevel =
                    minVolumeLevel + (1.0f - minVolumeLevel) * (progress.toFloat() / maxProgress)
                mediaPlayerCafe?.setVolume(volumeLevel, volumeLevel)
                mediaPlayerCafe?.setVolume(volumeLevel, volumeLevel)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Nothing to do here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Nothing to do here
            }
        })

        rainSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Calculate the volume level based on progress
                val volumeLevel =
                    minVolumeLevel + (1.0f - minVolumeLevel) * (progress.toFloat() / maxProgress)
                mediaPlayerRain?.setVolume(volumeLevel, volumeLevel)
                mediaPlayerRain?.setVolume(volumeLevel, volumeLevel)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Nothing to do here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Nothing to do here
            }
        })

        natureSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Calculate the volume level based on progress
                val volumeLevel =
                    minVolumeLevel + (1.0f - minVolumeLevel) * (progress.toFloat() / maxProgress)
                mediaPlayerNature?.setVolume(volumeLevel, volumeLevel)
                mediaPlayerNature?.setVolume(volumeLevel, volumeLevel)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Nothing to do here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Nothing to do here
            }
        })
        changeImage.setOnClickListener {
            startActivity(Intent(this, ChangeRoomImage::class.java))
        }




        val rainPlay :FloatingActionButton = findViewById(R.id.rainBtn)
        val cafePlay : FloatingActionButton = findViewById(R.id.cafeBtn)
        val nature : FloatingActionButton = findViewById(R.id.natureBtn)
        val firePlay : FloatingActionButton = findViewById(R.id.firePlaceBtn)
        firePlay.setOnClickListener {
            //mediaPlayer?.release()
            mediaPlayerFire = MediaPlayer.create(this, R.raw.fireplace)

            // Set the initial volume based on the SeekBar progress
            val initialVolumeLevel = minVolumeLevel + (maxVolumeLevel - minVolumeLevel) * (fireSeekBar.progress.toFloat() / maxProgress)
            mediaPlayerFire?.setVolume(initialVolumeLevel, initialVolumeLevel)


            mediaPlayerFire?.start()
        }

        rainPlay.setOnClickListener {
            //mediaPlayer?.release()
            mediaPlayerRain = MediaPlayer.create(this, R.raw.rain)

            // Set the initial volume based on the SeekBar progress
            val initialVolumeLevel = minVolumeLevel + (maxVolumeLevel - minVolumeLevel) * (rainSeekBar.progress.toFloat() / maxProgress)
            mediaPlayerRain?.setVolume(initialVolumeLevel, initialVolumeLevel)


            mediaPlayerRain?.start()
        }

        cafePlay.setOnClickListener {
            //mediaPlayer?.release()
            mediaPlayerCafe = MediaPlayer.create(this, R.raw.cafe)

            // Set the initial volume based on the SeekBar progress
            val initialVolumeLevel = minVolumeLevel + (maxVolumeLevel - minVolumeLevel) * (cafeSeekBar.progress.toFloat() / maxProgress)
            mediaPlayerCafe?.setVolume(initialVolumeLevel, initialVolumeLevel)


            mediaPlayerCafe?.start()
        }
        nature.setOnClickListener {
            //mediaPlayer?.release()
            mediaPlayerNature = MediaPlayer.create(this, R.raw.nature)

            // Set the initial volume based on the SeekBar progress
            val initialVolumeLevel = minVolumeLevel + (maxVolumeLevel - minVolumeLevel) * (natureSeekBar.progress.toFloat() / maxProgress)
            mediaPlayerNature?.setVolume(initialVolumeLevel, initialVolumeLevel)


            mediaPlayerNature?.start()
        }


        cafeStop.setOnClickListener {
            mediaPlayerCafe?.run { stop() }
        }

        fireStop.setOnClickListener {
            mediaPlayerFire?.run { stop() }
        }

        rainStop.setOnClickListener {
            mediaPlayerRain?.run { stop() }
        }

        natureStop.setOnClickListener {
            mediaPlayerNature?.run { stop() }
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


        val settingBtn : TextView = findViewById(R.id.nav_settings)
        settingBtn.setOnClickListener {
            viewModel.switchToSettings()
        }

        val changeName: ImageView = findViewById(R.id.changeRoomSongButton)
        changeName.setOnClickListener {
            val intent = Intent(this, SearchTrack::class.java)
            constants.changeRoom = true
            startActivity(intent)
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