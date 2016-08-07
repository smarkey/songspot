package com.songspot

import grails.plugins.rest.client.RestBuilder
import org.joda.time.DateTime

class SpotifyController {
    def utilitiesService

    static final String authorizationUrl = "https://accounts.spotify.com/authorize"
    static final String rootUrl = "https://api.spotify.com/v1"
    static final String clientId = "6cd61f6ef9ed4635bb4342f9b137a374"
    static final String clientSecret = "c7f7eb3e1db1480abda45610c80c4f9b"
    static final RestBuilder rest = new RestBuilder()

    def authorize() {
        redirect(url: "$authorizationUrl?" +
                "response_type=code&" +
                "scope=playlist-modify-private&" +
                "client_id=$clientId&" +
                "redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fspotify%2Fcallback&" +
                "state=NA&" +
                "show_dialog=false")
    }

    def callback() {
        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        songSpotUserConfig.spotifyAuthorizationCode = params.code
        songSpotUserConfig.save()
        redirect(action:"getToken")
    }

    def getToken() {
        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        String spotifyAuthorizationCode = songSpotUserConfig.getSpotifyAuthorizationCode()

        def resp = rest.post("https://accounts.spotify.com/api/token?grant_type=authorization_code&code=$spotifyAuthorizationCode&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fspotify%2Fcallback") {
            header "Content-Type", "application/x-www-form-urlencoded"
            header "Authorization", "Basic ${"$clientId:$clientSecret".bytes.encodeBase64().toString()}"
        }

        DateTime spotifyRefreshTokenExpiry = new DateTime().plusSeconds(resp.json."expires_in".toInteger())
        songSpotUserConfig.spotifyAccessToken = resp.json.access_token
        songSpotUserConfig.spotifyRefreshToken = resp.json.refresh_token
        songSpotUserConfig.spotifyRefreshTokenExpiry = spotifyRefreshTokenExpiry.toDate()
        songSpotUserConfig.save()

        redirect(action:"createPlaylist", params:[playlistName:"SongSpot Playlist"])
    }

    def createPlaylist() {
        utilitiesService.refreshTokenIfNecessary()

        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = songSpotUserConfig.getSpotifyAccessToken()
        String spotifyUserId = utilitiesService.getSpotifyUserId()

        def resp = rest.post("$rootUrl/users/$spotifyUserId/playlists?name=${params.playlistName}&public=false") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
            json name:"${params.playlistName}",public:false
        }
        render resp.json
    }

    def addTrackToPlaylist() {
        utilitiesService.refreshTokenIfNecessary()

        String spotifyUserId = utilitiesService.getSpotifyUserId()
        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = songSpotUserConfig.getSpotifyAccessToken()

        def resp = rest.post("$rootUrl/users/{user_id}/playlists/{playlist_id}/tracks") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Accept", "application/json"
            json name:playlistName,public:false
        }
        render resp.json
    }
}
