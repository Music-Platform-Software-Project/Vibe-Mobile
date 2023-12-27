package view


import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.DashboardViewModel

class Dashboard : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        viewModel.setContext(this)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        viewModel.refreshPlaylists()
        viewModel.refreshRealPlaylists()
        viewModel.setRecyclerViewForArtists(emptyList())
        viewModel.setRecyclerViewForTracks(emptyList())


        val playlistAddButton : ImageView = findViewById(R.id.btnAddPlaylist)
        val realPlaylistAddButton : ImageView = findViewById(R.id.btnAddPlaylist2)


        val settingsBtn : TextView = findViewById(R.id.nav_settings)
        settingsBtn.setOnClickListener {
            viewModel.switchToSettings()
        }



        playlistAddButton.setOnClickListener {
            //showInputDialog()
            //viewModel.addRealPlaylist("Liked Songs")
            viewModel.addPlaylist("Liked Songs")
            playlistAddButton.visibility = View.GONE   //REMOVE THIS AFTER MVP
        }

        realPlaylistAddButton.setOnClickListener {
            showPlaylistInputDialog()

        }

        //REMOVE THIS AFTER MVP
        viewModel.isThereALikedPL { result ->
            Log.e("checker return", result.toString())
            // If result is greater than 0, hide the button
            if (result > 0) {
                playlistAddButton.visibility = View.GONE

            }
        }





    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        when (item.itemId) {
            R.id.action_search -> {
                showSearchDialog()
                return true
            }
            R.id.action_plus -> {
                //startActivity(Intent(this, ImportTrack::class.java))
                showImportDialog()

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun goToProfile(view: View) {
        startActivity(Intent(this, ProfilePage::class.java))
    }

    fun goToMyRoom(view: View){
        startActivity(Intent(this, my_room::class.java))
    }

    private fun showInputDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter the name of your Liked Songs collection")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val userInput = input.text.toString()
            // Handle the user input here
            // For example, you can display it in a Toast
            viewModel.addPlaylist(userInput)

        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showPlaylistInputDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter the name of your Playlist")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val userInput = input.text.toString()
            // Handle the user input here
            // For example, you can display it in a Toast
            viewModel.addRealPlaylist(userInput)

        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }



    private fun showSearchDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.search_dialog, null)
        val buttonPlaylist = dialogView.findViewById<Button>(R.id.buttonPlaylist)
        val buttonTrack = dialogView.findViewById<Button>(R.id.buttonTrack)

        builder.setView(dialogView)

        val dialog = builder.create()
        buttonPlaylist.setOnClickListener {
            viewModel.switchToSearchPlaylist()
            dialog.dismiss()
        }

        buttonTrack.setOnClickListener {
            viewModel.switchToSearchTrack()
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun showImportDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.import_dialog, null)
        val buttonSpoti = dialogView.findViewById<Button>(R.id.buttonSpoti)
        val buttonManuel = dialogView.findViewById<Button>(R.id.buttonManuel)

        builder.setView(dialogView)

        val dialog = builder.create()
        buttonSpoti.setOnClickListener {
            startActivity(Intent(this, ImportTrackSpotify::class.java))
            dialog.dismiss()
        }

        buttonManuel.setOnClickListener {
            startActivity(Intent(this, ImportTrack::class.java))
            dialog.dismiss()
        }

        dialog.show()
    }




}
