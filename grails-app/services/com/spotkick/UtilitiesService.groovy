package com.spotkick

import grails.transaction.Transactional

@Transactional
class UtilitiesService {
    def springSecurityService

    def getUserConfig() {
        SpotkickUser spotkickUser = springSecurityService.getCurrentUser()
        spotkickUser.getSpotkickUserConfig()
    }

    def handleResponse(resp) {
        switch(resp.statusCode.value) {
            case 200: log.info("Scup"); break;
            case 400: log.error("Bad Request: $resp.json.error"); break;
            case 401: log.error("Unauthorized: The request requires user authentication."); break;
            case 403: log.error("Forbidden: The server understood the request, but is refusing to fulfill it."); break;
            case 404: log.error("Not Found: The requested resource could not be found."); break;
            case 429: log.error("Too Many Requests: Rate limiting"); break;
            case 500: log.error("Internal Server Error: Naughty Spotify!"); break;
            case 502: log.error("Bad Gateway: The server was acting as a gateway or proxy and received an invalid response from the upstream server."); break;
            case 503: log.error("Service Unavailable: Naughty Spotify!"); break;
            default: break;
        }

        resp.json
    }
}
