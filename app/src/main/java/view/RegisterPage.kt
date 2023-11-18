package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

        btn.setOnClickListener {
            val email : String = findViewById<EditText>(R.id.registerEmailField).text.toString()
            val username : String = findViewById<EditText>(R.id.registerUsernameField).text.toString()
            val password : String = findViewById<EditText>(R.id.registerPasswordField).text.toString()
            val passAgain : String = findViewById<EditText>(R.id.registerPasswordAgainField).text.toString()

            if (password != passAgain){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()

            }
            else{
                Log.e("registration process", "Email is: $email")
                Log.e("registration process", "username is: $username")
                Log.e("registration process", "password is: $password")
                viewModel.registerUser(email, username, password)
            }
        }

    }
}