package viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import model.RegisterPayload
import model.RegisterResponse
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
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(constants.baseURL)
                .build()
                .create(APIRequest::class.java)

            val request = RegisterPayload(email, username, password)

            val retrofitData = retrofitBuilder.registerUser(request)
            Log.e("registration process", "going...")

            retrofitData.enqueue(object: Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    Log.e("registration response:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("registration response: ", response.body().toString())
                        ctx.startActivity(Intent(ctx, LoginPage::class.java))
                    } else {
                        Log.e("registration error: ", "HTTP ${response.code()}: ${response.message()}")
                        Log.e("registration error: ", response.message())
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
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