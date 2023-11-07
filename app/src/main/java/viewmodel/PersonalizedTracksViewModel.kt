package viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import network.RetrofitClient
import view.LoginPage
import view.PersonalizedTracks

class PersonalizedTracksViewModel() : ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: PersonalizedTracks) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

}