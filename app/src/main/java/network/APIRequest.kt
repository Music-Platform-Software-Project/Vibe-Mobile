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
        @Header("Authorization") bearerToken: String
    ): Call<RequestDataInterface.getUserDataResponse>

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

    @GET("/invitation/listSent")
    @Headers(constants.contentType)
    fun getSentRequests(
        @Header("Authorization") bearerToken: String
    ):Call<List<RequestDataInterface.sentFriendRequests>>

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


    @POST("/invitation/remove")
    @Headers(constants.contentType)
    fun rejectSentRequest(
        @Header("Authorization") bearerToken: String,
        @Body request: RequestDataInterface.acceptRequestPayload
    ):Call<String>

    @POST("/track/add")
    @Headers(constants.contentType)
    fun addTrack(
        //@Header("Authorization") bearerToken: String,
        @Body request: RequestDataInterface.addTrackRequest
    ):Call<List<RequestDataInterface.addTrackResponse>>

    @POST("/playlist/add")
    @Headers(constants.contentType)
    fun addPlaylist(
        @Header("Authorization") bearerToken: String,
        @Body request: RequestDataInterface.addPlaylistRequest
    ):Call<Boolean>

    @GET("/playlist/getOwn")
    @Headers(constants.contentType)
    fun getOwnPlaylists(
        @Header("Authorization") bearerToken: String
    ):Call<List<RequestDataInterface.MyPlaylistsResponse>>

    @POST("/playlist/get")
    @Headers(constants.contentType)
    fun searchPlaylist(
        @Body request: RequestDataInterface.PlaylistTrackSearchRequest
    ):Call<List<RequestDataInterface.SearchPlaylistResponse>>

    @POST("/track/get")
    @Headers(constants.contentType)
    fun searchTrack(
        @Header("Authorization") bearerToken: String,
        @Body request: RequestDataInterface.PlaylistTrackSearchRequest
    ):Call<List<RequestDataInterface.SearchTrackResponse>>

    @POST("/track/getDetail")
    @Headers(constants.contentType)
    fun getTrackDetails(
        @Header("Authorization") bearerToken: String,
        @Body request: RequestDataInterface.TrackDetailRequest
    ): Call<RequestDataInterface.TrackDetailResponse>


}