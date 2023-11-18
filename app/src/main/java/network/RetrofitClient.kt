package network

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(private val ctx: Context) {

    fun createAPIRequest(): APIRequest {
        val retrofit = Retrofit.Builder()
            .baseUrl(constants.baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(APIRequest::class.java)
    }


    fun createAPIRequestWithToken(token: String): APIRequest {
        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            Log.d("API Request", "URL: ${newRequest.url}")
            Log.d("API Request", "Headers: ${newRequest.headers}")
            chain.proceed(newRequest)
        }.build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(constants.baseURL)
            .client(okHttpClient)
            .build()

        return retrofit.create(APIRequest::class.java)
    }
}