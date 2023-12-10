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
        val album: Album,
        val artists: List<Artist>,
        val name: String,
        val genre: String,
        val duration_ms: Int,
        val tempo: Int,
        val instrumentalness: Int,
        val acousticness: Int,
        val energy: Int
    )

    data class Album(
        val album_type: String,
        val artists: List<Artist>,
        val genres: List<String>,
        val name: String,
        val release_year: Int,
        val total_tracks: Int
    )

    data class Artist(
        val genres: List<String>,
        val name: String
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
        val artists: List<artiz>,
        val duration_ms : Int,
        val rating: Double
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
        val duration_ms: Int,
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
        val id: String,
        val name: String,
        val album: AlbumForTrackDetails,
        val genre: String,
        val artists: List<String>,
        val rating: Double,
        val duration_ms: Int,
        val tempo: Int,
        val instrumentalness: Int,
        val acousticness: Int,
        val energy: Int
    )


    data class artiz(
        val name: String
    )
    data class AlbumForTrackDetails(
        val album_type: String,
        val name: String,
        val release_year: Int,
        val total_tracks: Int
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
        val duration_ms: Int,
        val rating: Int,
    )

    data class TrackRateRequest(
        val id: String,
        val rate : Int
    )

    data class RecommendTrackResponse(
        val id: String,
        val username: String,
        val ratedTracks: List<RatedTrack>
    )

    data class RatedTrack(
        val id: String,
        val artists: List<String>,
        val name: String,
        val duration_ms: Int,
        val rating: Double
    )


    data class UserStatsRequest(
        val days : Int
    )

    data class UserStatResponse(
        val userRate: Int,
        val rateTime: String,
        val track: UserStatTrack
    )

    data class UserStatTrack(
        val id: String,
        val tempo: Int,
        val instrumentalness: Int,
        val acousticness: Int,
        val energy: Int,
        val genre: String,
        val artists: List<String>
    )




}