package view

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.ProfilePageViewModel
import viewmodel.SeeAllFriendsViewModel

class SeeAllFriends : AppCompatActivity() {
    private lateinit var viewModel : SeeAllFriendsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_friends)
        viewModel = ViewModelProvider(this).get(SeeAllFriendsViewModel::class.java)
        viewModel.setContext(this)

        viewModel.seeAllFriends()

        val goback = findViewById<ImageButton>(R.id.goBackBtn)
        goback.setOnClickListener {
            viewModel.goBack()
        }



    }





}