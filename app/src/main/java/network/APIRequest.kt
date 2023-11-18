package network

import model.RequestDataInterface
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIRequest {

    @POST("/auth/register")
    @Headers(constants.contentType)
    fun registerUser(
        @Body request: RequestDataInterface.RegisterPayload
    ): Call<String>


    @POST("/auth/login")
    @Headers(constants.contentType)
    fun loginUser(
        @Body request: RequestDataInterface.LoginPayload
    ): Call<String>

    @POST("/auth/forgotPassword")
    @Headers(constants.contentType)
    fun forgotPassword(
        @Body request: RequestDataInterface.ForgotPasswordRequest
    ): Call<String>


    @POST("/user/get")
    @Headers(constants.contentType)
    fun userGetData(
    ): Call<String>

    @POST("/user/update")
    @Headers(constants.contentType)
    fun updateUser(
        @Body request: RequestDataInterface.UpdateUserRequest
    ): Call<String>

    @POST("/user/delete")
    @Headers(constants.contentType)
    fun deleteUser():Call<String>



}