package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cs308_00.R
import com.example.cs308_00.databinding.ActivityFriendsRoomBinding
import com.example.cs308_00.databinding.ActivityProfilePageBinding
import viewmodel.FriendsRoomViewModel
import viewmodel.ProfilePageViewModel

class ProfilePage : AppCompatActivity() {
    private lateinit var binding : ActivityProfilePageBinding
    private lateinit var viewModel : ProfilePageViewModel
    lateinit var toggle : ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        //var navBarBtn = findViewById<ImageButton>(R.id.navBarBtn)
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout1)


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }



}