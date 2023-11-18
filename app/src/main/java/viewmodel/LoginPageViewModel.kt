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
import view.Dashboard
import view.ForgotPassword
import view.LoginPage
import view.RegisterPage

class LoginPageViewModel() : ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient

    fun setContext(ctx:LoginPage) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun goRegister(){
        val i = Intent(ctx, RegisterPage::class.java)
        ctx.startActivity(i)
    }

    fun goForgotPassword() {
        val i = Intent(ctx, ForgotPassword::class.java)
        ctx.startActivity(i)
    }

    fun goToDashboard() {
        val i = Intent(ctx, Dashboard::class.java)
        ctx.startActivity(i)
    }


    fun loginUser(email: String, password: String) {

        try {
            val retrofitBuilder = retrofitClient.createAPIRequest()

            val request = RequestDataInterface.LoginPayload(email, password)

            val retrofitData = retrofitBuilder.loginUser(request)
            Log.e("login process", "going...")

            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("login response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        constants.bearerToken = responseBody.toString()
                        Log.e("token tag", "Bearer Token is: ${constants.bearerToken}")
                        Log.e("login response: ", responseBody ?: "Response body is null")
                        // Parse the responseBody as needed or handle the string response
                        goToDashboard()
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("login error: ", "HTTP ${response.code()}: $errorBody")
                            Toast.makeText(ctx, errorBody, Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Log.e("login error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("login error: ", t.toString())
                }
            })

        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }

    }
}