package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.DahsboardViewModel
import viewmodel.LoginPageViewModel

class LoginPage : AppCompatActivity() {

    private lateinit var viewModel: LoginPageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        viewModel = ViewModelProvider(this).get(LoginPageViewModel::class.java)
        viewModel.setContext(this)

        //going to the register page
        val registerButton = findViewById<Button>(R.id.createAccountBtn)
        val forgotButton = findViewById<Button>(R.id.btnForgotPassword)
        val loginButton = findViewById<Button>(R.id.loginBtn)
        registerButton.setOnClickListener {
            viewModel.goRegister()
        }

        forgotButton.setOnClickListener {
            viewModel.goForgotPassword()
        }
        loginButton.setOnClickListener {
            viewModel.goToDashboard()
        }



    }
}