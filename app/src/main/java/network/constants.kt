package network

import model.RequestDataInterface

object constants {
    //declare global constants here
    const val baseURL = "http://64.226.98.158:8080/"
    const val contentType = "Content-Type: application/json"
    const val  acceptType = "accept: application/json"
    var bearerToken = ""
    var currentPlaylistID = ""
    var friendsList = mutableListOf<RequestDataInterface.friends>()
    var likedSongsId = ""

}