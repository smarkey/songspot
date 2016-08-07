package com.songspot

import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import org.joda.time.DateTime

@Transactional
class UtilitiesService {
    def springSecurityService
    static final RestBuilder rest = new RestBuilder()

    def getUserConfig() {
        SongSpotUser songSpotUser = springSecurityService.getCurrentUser()
        songSpotUser.getSongSpotUserConfig()
    }

    def getSpotifyUserId() {
        refreshTokenIfNecessary()

        SongSpotUserConfig songSpotUserConfig = getUserConfig()
        String spotifyAccessToken = songSpotUserConfig.getSpotifyAccessToken()

        def respForId = rest.get("https://api.spotify.com/v1/me") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
        }
        respForId.json.id
    }

    def refreshTokenIfNecessary(String refreshToken) {
        SongSpotUserConfig songSpotUserConfig = getUserConfig()
        DateTime refreshTokenExpiry = new DateTime(songSpotUserConfig.spotifyRefreshTokenExpiry)

        if(refreshTokenExpiry < DateTime.now()) {
            String spotifyAuthorizationCode = songSpotUserConfig.getSpotifyAuthorizationCode()

            def resp = rest.post("https://accounts.spotify.com/api/token?grant_type=refresh_token&refresh_token=$refreshToken") {
                header "Content-Type", "application/x-www-form-urlencoded"
                header "Authorization", "Basic ${"$clientId:$clientSecret".bytes.encodeBase64().toString()}"
            }

            songSpotUserConfig.spotifyAccessToken = resp.json.access_token
        }
    }
}
