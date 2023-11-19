package network

import model.RequestDataInterface
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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


    @GET("/invitation/list")
    @Headers(constants.contentType)
    fun getRequests(
        @Header("Authorization") bearerToken: String
    ):Call<List<RequestDataInterface.friendRequests>>

    @POST("/invitation/send")
    @Headers(constants.contentType)
    fun sendRequest(
        @Header("Authorization") bearerToken: String,
        @Body request: RequestDataInterface.friendRequestPayload
    ):Call<String>


    @POST("/invitation/accept")
    @Headers(constants.contentType)
    fun acceptRequest(
        @Header("Authorization") bearerToken: String,
        @Body request: RequestDataInterface.acceptRequestPayload
    ):Call<String>

    @POST("/invitation/reject")
    @Headers(constants.contentType)
    fun rejectRequest(
        @Header("Authorization") bearerToken: String,
        @Body request: RequestDataInterface.acceptRequestPayload
    ):Call<String>


}