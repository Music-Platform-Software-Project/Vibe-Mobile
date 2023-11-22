package view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import com.example.cs308_00.databinding.ActivityProfilePageBinding
import viewmodel.ProfilePageViewModel

class ProfilePage : AppCompatActivity() {
    private lateinit var binding : ActivityProfilePageBinding
    private lateinit var viewModel : ProfilePageViewModel
    lateinit var toggle : ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        viewModel = ViewModelProvider(this).get(ProfilePageViewModel::class.java)
        viewModel.setContext(this)

        //var navBarBtn = findViewById<ImageButton>(R.id.navBarBtn)
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout1)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel.setRecyclerView()

        viewModel.setRecyclerViewForArtists()
        viewModel.setRecyclerViewForTracks()

        viewModel.setUsernameandFriends()

        val friendButton = findViewById<TextView>(R.id.manageFriends)
        friendButton.setOnClickListener {
            startActivity(Intent(this, ManageFriends::class.java))
        }

        val seeFriend = findViewById<Button>(R.id.seeFriends)
        seeFriend.setOnClickListener {
            viewModel.switchToSeeAllFriends()
        }

        val importBtn = findViewById<Button>(R.id.btnImport)
        importBtn.setOnClickListener {
            viewModel.switchToImportTrack()
        }


        val sendFriendRequestButton = findViewById<Button>(R.id.profileAddFriendsBtn)
        sendFriendRequestButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.profileAddFriendsField).text.toString()
            val editText = findViewById<EditText>(R.id.profileAddFriendsField)
            editText.text = Editable.Factory.getInstance().newEditable("")
            Toast.makeText(this, "friend request sent", Toast.LENGTH_SHORT).show()
            viewModel.sendRequest(username)
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

    fun goMyRoom(view: View) {
        startActivity(Intent(this, my_room::class.java))
    }

    fun goToPersonalizedTracks(view: View){
        startActivity(Intent(this, PersonalizedTracks::class.java))
    }



}