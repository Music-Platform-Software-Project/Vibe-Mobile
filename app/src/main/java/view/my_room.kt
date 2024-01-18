package view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import com.nitish.typewriterview.TypeWriterView
import com.squareup.picasso.Picasso
import viewmodel.DashboardViewModel

class my_room : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var viewModel: DashboardViewModel

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

        /*
        viewModel.setRecyclerView()
        viewModel.setRecyclerViewForArtists()
        viewModel.setRecyclerViewForTracks()

         */
        val personalRoomTrack : TypeWriterView = findViewById(R.id.personalRoomTrack)
        personalRoomTrack.setCharacterDelay(150)
       personalRoomTrack.animateText("After Hours")
        //personalRoomTrack.animate()
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