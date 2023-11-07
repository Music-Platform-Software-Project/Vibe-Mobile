package view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cs308_00.R

class ConfirmCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_code)

        val continueButton = findViewById<Button>(R.id.btn_confirm)
        continueButton.setOnClickListener {
            val code : Editable = findViewById<EditText>(R.id.codeField).text
            if(code.isNotEmpty() && code.isNotBlank()){
                startActivity(Intent(this, ResetPassword::class.java))
            }
        }
    }
}