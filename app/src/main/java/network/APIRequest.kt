package network

import model.RegisterPayload
import model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIRequest {

    @POST("/auth/register")
    @Headers("Content-Type: application/json")
    fun registerUser(
        @Body request: RegisterPayload
    ): Call<RegisterResponse>
}