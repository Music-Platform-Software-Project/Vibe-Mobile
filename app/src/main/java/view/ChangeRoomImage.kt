package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface
import viewmodel.AddRoomTrackViewModel
import viewmodel.ChangeImageViewModel
import viewmodel.RoomImageAdapter




class ChangeRoomImage : AppCompatActivity() {
    private lateinit var viewModel : ChangeImageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this).get(ChangeImageViewModel::class.java)
        viewModel.setContext(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_room_image)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel.getImages()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the home button click
                onBackPressed() // This will call super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}