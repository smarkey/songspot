package com.spotkick.thirdparty

import com.spotkick.SpotkickUserConfig
import grails.util.Environment

class UpstreamController {
    def utilitiesService
    def upstreamService
    def springSecurityService

    def authorize() {
        log.info("Redirecting to Spotify Authentication.")

        String authUrl = grailsApplication.config.com.spotkick.spotify.authUrl
        String clientId = grailsApplication.config.com.spotkick.spotify.clientId
        String redirectUri = grailsApplication.config.com.spotkick.spotify."${Environment.isDevelopmentMode() ? 'testCallback' : 'liveCallback'}"
        String scopes = "user-read-email playlist-modify-private"
        redirect(url: "$authUrl?" +
                "response_type=code&" +
                "scope=${URLEncoder.encode(scopes)}&" +
                "client_id=$clientId&" +
                "redirect_uri=$redirectUri&" +
                "state=NA&" +
                "show_dialog=false")
    }

    def callback() {
        log.info("Authentication complete.")

        log.info("Fetching Access Token.")
        def map = upstreamService.getToken(params.code)

        log.info("Creating User if necessary.")
        String username = upstreamService.getSpotifyUserEmailAddress(map.spotifyAccessToken)
        String password = "ehrmehrgehrdlookuthehrbutt"
        upstreamService.createSpotkickUserIfNecessary([username:username, password:password], "ROLE_ADMIN")

        springSecurityService.reauthenticate(username, password)
        log.info("Logged in as ${springSecurityService.getCurrentUser().getUsername()}")

        log.info("Storing Authorization Code, Access Token and Refresh Token.")
        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        spotkickUserConfig.spotifyAuthorizationCode = params.code
        spotkickUserConfig.spotifyAccessToken = map.spotifyAccessToken
        spotkickUserConfig.spotifyRefreshToken = map.spotifyRefreshToken
        spotkickUserConfig.spotifyRefreshTokenExpiry = map.spotifyRefreshTokenExpiry
        spotkickUserConfig.save(flush:true)
        redirect(controller:"main", action:"index")
    }
}
