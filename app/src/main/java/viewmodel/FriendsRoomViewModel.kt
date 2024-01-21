package viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import network.RetrofitClient
import view.Dashboard
import view.FriendsRoom
import view.ProfilePage
import view.Settings

class FriendsRoomViewModel() : ViewModel() {
    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: FriendsRoom) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun switchToDashboard(){
        val intent = Intent(ctx, Dashboard::class.java)
        ctx.startActivity(intent)
    }

    fun switchToProfile(){
        val intent = Intent(ctx, ProfilePage::class.java)
        ctx.startActivity(intent)
    }

    fun switchToSetting(){
        val intent = Intent(ctx, Settings::class.java)
        ctx.startActivity(intent)
    }




}