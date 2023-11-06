package view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.cs308_00.R

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val continueButton = findViewById<Button>(R.id.btn_continue)
        continueButton.setOnClickListener {
            val i = Intent(this, ConfirmCode::class.java)
            startActivity(i)
        }
    }
}