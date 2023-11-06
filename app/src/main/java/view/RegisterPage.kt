package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.RegisterPageViewModel

class RegisterPage : AppCompatActivity() {

    private lateinit var viewModel : RegisterPageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        viewModel = ViewModelProvider(this).get(RegisterPageViewModel::class.java)
        viewModel.setContext(this)

        val btn : Button = findViewById(R.id.btnRegister)
        val email : String = findViewById<EditText>(R.id.registerEmailField).text.toString()
        val username : String = findViewById<EditText>(R.id.registerUsernameField).text.toString()
        val password : String = findViewById<EditText>(R.id.registerPasswordField).text.toString()
        val passAgain : String = findViewById<EditText>(R.id.registerPasswordAgainField).text.toString()
        btn.setOnClickListener {
            viewModel.registerUser(email, username, password, passAgain)
        }


    }
}