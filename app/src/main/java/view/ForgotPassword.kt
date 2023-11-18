package view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.ForgotPasswordViewModel
import viewmodel.RegisterPageViewModel

class ForgotPassword : AppCompatActivity() {

    private lateinit var viewModel : ForgotPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
        viewModel.setContext(this)


        val continueButton = findViewById<Button>(R.id.btn_continue)

        continueButton.setOnClickListener {
            val email : String = findViewById<EditText>(R.id.emailField).text.toString()
            Log.e("Forgot Pass", "Email: $email")
            viewModel.forgotPassword(email)
        }
    }
}