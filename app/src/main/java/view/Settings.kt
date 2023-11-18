package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.cs308_00.R
import viewmodel.RegisterPageViewModel
import viewmodel.SettingsViewModel

class Settings : AppCompatActivity() {

    private lateinit var viewModel : SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        viewModel.setContext(this)

        val submitBtn : Button = findViewById(R.id.btnChangeUsername)
        val backBtn : ImageButton = findViewById(R.id.backBtn)
        val deleteBtn : Button = findViewById(R.id.btnDeleteAccount)
        backBtn.setOnClickListener {
            viewModel.backToDashboard()
        }
        submitBtn.setOnClickListener {
            val newUsername : String = findViewById<EditText>(R.id.newUsername).text.toString()
            if (newUsername.isEmpty() || newUsername.isBlank()){
                Toast.makeText(this, "Please enter a new username", Toast.LENGTH_LONG).show()
            }
            else{
                viewModel.updateUsername(newUsername)
            }
        }


        deleteBtn.setOnClickListener {
            viewModel.deleteUser()
        }


    }
}