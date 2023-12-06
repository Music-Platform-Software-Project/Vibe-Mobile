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

    data class addTrackRequest(
        val track: TrackData
    )

    data class TrackData(
        val name: String,
        val artists: List<String>,
        val album: String,
        val genre: String,
        val tempo: Int,
        val acousticness: Int,
        val energy: Int,
        val instrumentalness: Int,
        val mood: String,
        val duration : Int
    )

    data class addPlaylistRequest(
        val name: String
    )

    data class MyPlaylistsResponse(
        val id: String,
        val name: String,
        val ownerUsername: String = "TEST"
    )

    data class PlaylistTrackSearchRequest(
        val searchKey : String
    )

    data class SearchTrackResponse(
        val id: String,
        val name: String,
        val artists: List<String>
    )

    data class  SearchPlaylistResponse(
        val id: String,
        val name: String,
        val ownerUsername: String = "TEST"
    )

    data class Track(
        val id: String,
        val name: String,
        val artists: List<String>,
        val duration: Int,
        val rating: Double
    )

    data class TrackDetailRequest(
        val id : String
    )

    data class TrackDetailRequestPlaylist(
        val id : String,
        val trackId : String
    )

    data class TrackDetailResponse(
        val id : String,
        val name : String,
        val artists : List<String>,
        val album : String,
        val genre : String,
        val tempo : Int,
        val acousticness : Int,
        val energy : Int,
        val instrumentalness : Int,
        val mood : String,
        val duration: Int,
        val rating : Int
    )

    data class PlaylistDetailResponse(
        val id : String,
        val name : String,
        val trackCount: Int,
        val totalDuration: Int,
        val tracks : List<Track>,
        val ownerUsername: String = "TEST"
    )

    data class AddTrackToPlaylist(
        val id : String,
        val trackId: String
    )

    data class changePlaylistName(
        val id : String,
        val name: String
    )

    data class removeFriendRequest(

        val id : String
    )


    data class GetOwnTrackResponse(
        val id: String,
        val name: String,
        val artists: List<String>,
        val rating: Int,
        val duration: Int
    )




}