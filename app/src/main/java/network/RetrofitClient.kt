package network

import android.content.Context
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
}