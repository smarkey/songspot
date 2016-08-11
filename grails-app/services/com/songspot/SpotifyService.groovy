package com.songspot

import grails.plugins.rest.client.RestBuilder
import org.joda.time.DateTime

class SpotifyService {
    def utilitiesService
    def grailsApplication
    static final RestBuilder rest = new RestBuilder()

    def getSpotifyUserId() {
        refreshTokenIfNecessary()

        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = songSpotUserConfig.getSpotifyAccessToken()

        def resp = rest.get("https://api.spotify.com/v1/me") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
        }
        resp.json.id
    }

    def getToken() {
        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        String spotifyAuthorizationCode = songSpotUserConfig.getSpotifyAuthorizationCode()

        def resp = rest.post("https://accounts.spotify.com/api/token?grant_type=authorization_code&code=$spotifyAuthorizationCode&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fspotify%2Fcallback") {
            header "Content-Type", "application/x-www-form-urlencoded"
            header "Authorization", "Basic ${"$grailsApplication.config.com.songspot.spotify.clientId:$grailsApplication.config.com.songspot.spotify.clientSecret".bytes.encodeBase64().toString()}"
        }

        DateTime spotifyRefreshTokenExpiry = new DateTime().plusSeconds(resp.json."expires_in".toInteger())
        songSpotUserConfig.spotifyAccessToken = resp.json.access_token
        songSpotUserConfig.spotifyRefreshToken = resp.json.refresh_token
        songSpotUserConfig.spotifyRefreshTokenExpiry = spotifyRefreshTokenExpiry.toDate()
        songSpotUserConfig.save()
    }

    def refreshTokenIfNecessary(String refreshToken) {
        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        DateTime refreshTokenExpiry = new DateTime(songSpotUserConfig.spotifyRefreshTokenExpiry)

        if(refreshTokenExpiry < DateTime.now()) {
            def resp = rest.post("https://accounts.spotify.com/api/token?grant_type=refresh_token&refresh_token=$refreshToken") {
                header "Authorization", "Basic ${"$grailsApplication.config.com.songspot.spotify.clientId:$grailsApplication.config.com.songspot.spotify.clientSecret".bytes.encodeBase64().toString()}"
                header "Content-Type", "application/x-www-form-urlencoded"
            }

            songSpotUserConfig.spotifyAccessToken = resp.json.access_token
            songSpotUserConfig.save(flush:true)
        }
    }

    def createPlaylist(String name) {
        refreshTokenIfNecessary()

        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = songSpotUserConfig.getSpotifyAccessToken()
        String spotifyUserId = getSpotifyUserId()

        def resp = rest.post("$grailsApplication.config.com.songspot.spotify.url/users/$spotifyUserId/playlists?name=${name}&public=false") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
            json name:"${name}",public:false
        }
        return resp.json
    }

    def getArtistsTopTracks(String artistId) {
        refreshTokenIfNecessary()

        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = songSpotUserConfig.getSpotifyAccessToken()

        def resp = rest.get("$grailsApplication.config.com.songspot.spotify.url/artists/$artistId/top-tracks?country=GB") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
        }

        def result = []
        resp.json.tracks.each { track ->
            result << [
                    id: track.id,
                    name: track.name,
                    uri: track.uri
            ]
        }
        result
    }

    def findArtistByName(String artistName) {
        refreshTokenIfNecessary()

        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = songSpotUserConfig.getSpotifyAccessToken()
        String encodedArtistName = URLEncoder.encode(artistName)

        def resp = rest.get("$grailsApplication.config.com.songspot.spotify.url/search?q=$encodedArtistName&type=artist&market=GB&limit=1") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
        }
        resp.json.artists.items
    }

    def addTrackToPlaylist(trackUris, playlistId) {
        refreshTokenIfNecessary()

        String spotifyUserId = getSpotifyUserId()
        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = songSpotUserConfig.getSpotifyAccessToken()
        String encodedUris = trackUris.join(",")

        def resp = rest.post("$grailsApplication.config.com.songspot.spotify.url/users/$spotifyUserId/playlists/$playlistId/tracks?position=0&uris=$encodedUris") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Accept", "application/json"
        }
        resp.json
    }
}
