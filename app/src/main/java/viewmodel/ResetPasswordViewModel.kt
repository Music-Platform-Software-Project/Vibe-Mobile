package viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import network.RetrofitClient
import view.RegisterPage
import view.ResetPassword

class ResetPasswordViewModel() : ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: ResetPassword) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }
}