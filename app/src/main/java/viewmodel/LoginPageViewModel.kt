package viewmodel

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import network.RetrofitClient
import view.Dashboard
import view.ForgotPassword
import view.LoginPage
import view.RegisterPage

class LoginPageViewModel() : ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx:LoginPage) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun goRegister(){
        val i = Intent(ctx, RegisterPage::class.java)
        ctx.startActivity(i)
    }

    fun goForgotPassword() {
        val i = Intent(ctx, ForgotPassword::class.java)
        ctx.startActivity(i)
    }

    fun goToDashboard() {
        val i = Intent(ctx, Dashboard::class.java)
        ctx.startActivity(i)
    }
}