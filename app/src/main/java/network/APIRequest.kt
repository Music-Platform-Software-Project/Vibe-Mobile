package network

import model.LoginPayload
import model.RegisterPayload
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIRequest {

    @POST("/auth/register")
    @Headers("Content-Type: application/json")
    fun registerUser(
        @Body request: RegisterPayload
    ): Call<String>


    @POST("/auth/login")
    @Headers("Content-Type: application/json")
    fun loginUser(
        @Body request: LoginPayload
    ): Call<String>
}