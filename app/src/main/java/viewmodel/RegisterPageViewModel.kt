package viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import network.RetrofitClient
import view.LoginPage
import view.RegisterPage

class RegisterPageViewModel() :ViewModel() {
    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: RegisterPage) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun registerUser(email: String, username: String, password: String, passAgain: String) {
        ctx.startActivity(Intent(ctx, LoginPage::class.java))
    }
}