package view


import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
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

        /*
        viewModel.setRecyclerView()
        viewModel.setRecyclerViewForArtists()
        viewModel.setRecyclerViewForTracks()

         */

        val settingsBtn : TextView = findViewById(R.id.nav_settings)
        settingsBtn.setOnClickListener {
            viewModel.switchToSettings()
        }
        val playlistAddButton : ImageView = findViewById(R.id.btnAddPlaylist)
        playlistAddButton.setOnClickListener {
            showInputDialog()
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
                Toast.makeText(this, "search button clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_plus -> {
                startActivity(Intent(this, ImportTrack::class.java))
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
        builder.setTitle("Enter the name of the Playlist")

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


}