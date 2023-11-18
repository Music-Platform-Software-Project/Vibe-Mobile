package viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import model.RequestDataInterface
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import view.ConfirmCode
import view.ForgotPassword
import view.LoginPage

class ForgotPasswordViewModel() : ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: ForgotPassword) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }


    private fun switchToCode(){
        val intent = Intent(ctx, ConfirmCode::class.java)
        ctx.startActivity(intent)
    }
    fun forgotPassword(email : String){
        try {
            val retrofitBuilder = retrofitClient.createAPIRequest()

            val request = RequestDataInterface.ForgotPasswordRequest(email)
            val retrofitData = retrofitBuilder.forgotPassword(request)
            Log.e("Forgot Pass", "Process Started...")
            Log.e("Forgot Pass", "Email: $email")
            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("Api response:", "Response waiting: retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("Forgot Pass response: ", responseBody ?: "Response body is null")
                        // Parse the responseBody as needed or handle the string response
                        switchToCode()
                    }
                    else {
                        try {
                            val errorBody = response.errorBody()?.string()
                            Log.e("Forgot Password: ", "HTTP ${response.code()}: $errorBody")
                        } catch (e: Exception) {
                            Log.e("Forgot Password: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("Forgot Password error: ", t.toString())
                }
            })
        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }
}