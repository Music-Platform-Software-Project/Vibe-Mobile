package viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import network.RetrofitClient
import view.ImportTrack
import view.PersonalizedTracks
import view.ProfilePage

class ImportTrackViewModel() : ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: ImportTrack) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun backToProfile(){
        val i = Intent(ctx, ProfilePage::class.java)
        ctx.startActivity(i)
    }

}