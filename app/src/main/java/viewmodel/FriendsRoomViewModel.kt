package viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import network.RetrofitClient
import view.FriendsRoom

class FriendsRoomViewModel() : ViewModel() {
    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: FriendsRoom) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }


}