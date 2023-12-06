package view

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import view.ui.main.SectionsPagerAdapter
import com.example.cs308_00.databinding.ActivityManageFriendsBinding
import viewmodel.ProfilePageViewModel

class ManageFriends : AppCompatActivity() {

    private lateinit var binding: ActivityManageFriendsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManageFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

    }

    fun goBack(view: View) {
        val intent = Intent(this, ProfilePage::class.java)
        startActivity(intent)
        finish()

    }


}