package viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import model.RequestDataInterface
import network.APIRequest
import network.RetrofitClient
import network.constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import view.LoginPage
import view.RegisterPage

class RegisterPageViewModel() :ViewModel() {
    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx: RegisterPage) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun registerUser(email: String, username: String, password: String) {

        try {
            val retrofitBuilder = retrofitClient.createAPIRequest()

            val request = RequestDataInterface.RegisterPayload(email, username, password)

            val retrofitData = retrofitBuilder.registerUser(request)
            Log.e("registration process", "going...")

            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("registration response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("registration response: ", responseBody ?: "Response body is null")
                        constants.bearerToken = responseBody.toString()
                        // Parse the responseBody as needed or handle the string response
                        Toast.makeText(ctx, "Registration Successfull !", Toast.LENGTH_LONG).show()
                        ctx.startActivity(Intent(ctx, LoginPage::class.java))
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("registration error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Log.e("registration error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("registration error: ", t.toString())
                }
            })




        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }

    }
}