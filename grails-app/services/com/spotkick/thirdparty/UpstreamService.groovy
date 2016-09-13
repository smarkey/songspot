package com.spotkick.thirdparty

import com.spotkick.SpotkickUserConfig
import grails.plugins.rest.client.RestBuilder
import org.joda.time.DateTime
import grails.util.Environment

class UpstreamService {
    def utilitiesService
    def grailsApplication
    static final RestBuilder rest = new RestBuilder()

    def getSpotifyUserId() {
        refreshTokenIfNecessary()

        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = spotkickUserConfig.getSpotifyAccessToken()

        def resp = rest.get("https://api.spotify.com/v1/me") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
        }
        resp.json.id
    }

    def getToken() {
        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String spotifyAuthorizationCode = spotkickUserConfig.getSpotifyAuthorizationCode()
        String redirectUri = grailsApplication.config.com.spotkick.spotify."${Environment.isDevelopmentMode() ? 'testCallback' : 'liveCallback'}"
        String clientId = grailsApplication.config.com.spotkick.spotify.clientId
        String clientSecret = grailsApplication.config.com.spotkick.spotify.clientSecret

        def resp = rest.post("https://accounts.spotify.com/api/token?grant_type=authorization_code&code=$spotifyAuthorizationCode&redirect_uri=$redirectUri") {
            header "Content-Type", "application/x-www-form-urlencoded"
            header "Authorization", "Basic ${"$clientId:$clientSecret".bytes.encodeBase64().toString()}"
        }

        DateTime spotifyRefreshTokenExpiry = new DateTime().plusSeconds(resp.json."expires_in".toInteger())
        spotkickUserConfig.spotifyAccessToken = resp.json.access_token
        spotkickUserConfig.spotifyRefreshToken = resp.json.refresh_token
        spotkickUserConfig.spotifyRefreshTokenExpiry = spotifyRefreshTokenExpiry.toDate()
        spotkickUserConfig.save()
    }

    def refreshTokenIfNecessary() {
        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        DateTime refreshTokenExpiry = new DateTime(spotkickUserConfig.spotifyRefreshTokenExpiry)
        String refreshToken = spotkickUserConfig.getSpotifyRefreshToken()

        if(refreshTokenExpiry < DateTime.now()) {
            def resp = rest.post("https://accounts.spotify.com/api/token?grant_type=refresh_token&refresh_token=$refreshToken") {
                header "Authorization", "Basic ${"$grailsApplication.config.com.spotkick.spotify.clientId:$grailsApplication.config.com.spotkick.spotify.clientSecret".bytes.encodeBase64().toString()}"
                header "Content-Type", "application/x-www-form-urlencoded"
            }

            spotkickUserConfig.spotifyAccessToken = resp.json.access_token
            spotkickUserConfig.save(flush:true)
        }
    }

    def createPlaylist(String name) {
        refreshTokenIfNecessary()

        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = spotkickUserConfig.getSpotifyAccessToken()
        String spotifyUserId = getSpotifyUserId()

        def resp = rest.post("$grailsApplication.config.com.spotkick.spotify.url/users/$spotifyUserId/playlists?name=${name}&public=false") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
            json name:"${name}",public:false
        }
        return resp.json
    }

    def getArtistsTopTracks(String artistId, int numberOftracks = 5) {
        refreshTokenIfNecessary()

        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = spotkickUserConfig.getSpotifyAccessToken()

        def resp = rest.get("$grailsApplication.config.com.spotkick.spotify.url/artists/$artistId/top-tracks?country=GB") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
        }

        def result = []
        resp.json.tracks.eachWithIndex { track, idx ->
            if(idx < numberOftracks) {
                result << [
                        id  : track.id,
                        name: track.name,
                        uri : track.uri
                ]
            }
        }
        result
    }

    def findArtistByName(String artistName) {
        refreshTokenIfNecessary()

        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = spotkickUserConfig.getSpotifyAccessToken()
        String encodedArtistName = URLEncoder.encode(artistName)

        def resp = rest.get("$grailsApplication.config.com.spotkick.spotify.url/search?q=$encodedArtistName&type=artist&market=GB&limit=1") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
        }
        resp.json.artists.items
    }

    def addTrackToPlaylist(trackUris, playlistId) {
        refreshTokenIfNecessary()

        String spotifyUserId = getSpotifyUserId()
        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = spotkickUserConfig.getSpotifyAccessToken()
        String encodedUris = trackUris.join(",")

        def resp = rest.post("$grailsApplication.config.com.spotkick.spotify.url/users/$spotifyUserId/playlists/$playlistId/tracks?position=0&uris=$encodedUris") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Accept", "application/json"
        }
        resp.json
    }
}
