package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import org.w3c.dom.Text
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
            val email = findViewById<TextView>(R.id.usernameField).text.toString()
            val password = findViewById<TextView>(R.id.passwordField).text.toString()
            viewModel.loginUser(email, password)
        }



    }
}