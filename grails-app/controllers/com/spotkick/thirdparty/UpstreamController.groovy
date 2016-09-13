package com.spotkick.thirdparty

import com.spotkick.SpotkickUserConfig
import grails.util.Environment

class UpstreamController {
    def utilitiesService
    def upstreamService

    def authorize() {
        log.debug("Redirecting to Spotify Authentication.")

        String authUrl = grailsApplication.config.com.spotkick.spotify.authUrl
        String clientId = grailsApplication.config.com.spotkick.spotify.clientId
        String redirectUri = grailsApplication.config.com.spotkick.spotify."${Environment.isDevelopmentMode() ? 'testCallback' : 'liveCallback'}"

        redirect(url: "$authUrl?" +
                "response_type=code&" +
                "scope=playlist-modify-private&" +
                "client_id=$clientId&" +
                "redirect_uri=$redirectUri&" +
                "state=NA&" +
                "show_dialog=false")
    }

    def callback() {
        log.debug("Authentication complete.")
        if(params.code) {
            log.debug("Fetching and storing Authorization Code.")
            SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
            spotkickUserConfig.spotifyAuthorizationCode = params.code
            spotkickUserConfig.save()
        }
        log.debug("Fetching and storing Access Token.")
        upstreamService.getToken()
        redirect(controller:"main", action:"index")
    }
}
