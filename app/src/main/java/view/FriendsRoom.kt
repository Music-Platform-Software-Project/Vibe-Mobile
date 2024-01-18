package view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import com.squareup.picasso.Picasso
import viewmodel.FriendsRoomViewModel


class FriendsRoom : AppCompatActivity() {
    private lateinit var viewModel : FriendsRoomViewModel
    lateinit var toggle : ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_room)
        //viewModel = ViewModelProvider(this).get(FriendsRoomViewModel::class.java)
        //viewModel.setContext(this)

        //var navBarBtn = findViewById<ImageButton>(R.id.navBarBtn)
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout1)


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val friendUsername = intent.getStringExtra("friendName1").toString()
        Log.e("name in friend ", friendUsername.toString())
        val usernameTxt : TextView = findViewById(R.id.frinedsRoomTxt)
        usernameTxt.text = "Vibing in $friendUsername's Room"


        val roomImage : ImageView = findViewById(R.id.spotifyFrame)
        Picasso.with(this)
            .load(R.drawable.default_room) // Replace with your image resource or URL
            .fit()
            .into(roomImage)


        val dashBtn : TextView = findViewById(R.id.nav_dashboard)
        dashBtn.setOnClickListener {
            viewModel.switchToDashboard()
        }

        val profileBtn : TextView = findViewById(R.id.nav_profile)
        profileBtn.setOnClickListener {
            viewModel.switchToProfile()
        }
        
        val settingsBtn : TextView = findViewById(R.id.nav_settings)
        settingsBtn.setOnClickListener {
            viewModel.switchToSetting()
        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }
}