package view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import com.example.cs308_00.databinding.ActivityFriendsRoomBinding
import viewmodel.FriendsRoomViewModel


class FriendsRoom : AppCompatActivity() {
    private lateinit var binding : ActivityFriendsRoomBinding
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




    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }
}