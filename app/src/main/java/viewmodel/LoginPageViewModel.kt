package viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import model.LoginPayload
import model.RegisterPayload
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
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(constants.baseURL)
                .build()
                .create(APIRequest::class.java)

            val request = LoginPayload(email, password)

            val retrofitData = retrofitBuilder.loginUser(request)
            Log.e("login process", "going...")

            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("login response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("login response: ", responseBody ?: "Response body is null")
                        // Parse the responseBody as needed or handle the string response
                        goToDashboard()
                    }
                    else {
                        try {
                            val errorBody = response.errorBody()?.string()
                            Log.e("login error: ", "HTTP ${response.code()}: $errorBody")
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