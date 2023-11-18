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
}