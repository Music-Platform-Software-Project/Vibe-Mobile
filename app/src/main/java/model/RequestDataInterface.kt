package model

interface RequestDataInterface {

    data class RegisterPayload(
        val email: String,
        val username: String,
        val password: String
    )

    data class LoginPayload(
        val email: String,
        val password: String
    )

    data class ForgotPasswordRequest(
        val email: String
    )

    data class UpdateUserRequest(
        val username : String
    )

    data class friendRequests(
        val sender: String,
        val id: String
    )
    data class sentFriendRequests(
        val receiver: String,
        val id: String
    )

    data class getUserDataResponse(
        val email : String,
        val username: String,
        val active : Boolean,
        val friends : List<friends>
    )

    data class friends(
        val id : String,
        val username : String

    )

    data class friendRequestPayload(
        val username: String
    )

    data class acceptRequestPayload(
        val id: String
    )


}