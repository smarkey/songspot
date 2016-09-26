package com.spotkick

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional

import java.text.Normalizer

@Transactional
class UtilitiesService {
    SpringSecurityService springSecurityService

    def getUserConfig() {
        SpotkickUser spotkickUser = springSecurityService.getCurrentUser()
        spotkickUser.getSpotkickUserConfig()
    }

    def handleResponse(resp, detailMap) {
        switch(resp.statusCode.value) {
            case 200: log.debug("Scup"); break;
            case 400: log.error("Bad Request: $resp.json.error.\n$detailMap"); break;
            case 401: log.error("Unauthorized: The request requires user authentication.\n$detailMap"); break;
            case 403: log.error("Forbidden: The server understood the request, but is refusing to fulfill it.\n$detailMap"); break;
            case 404: log.error("Not Found: The requested resource could not be found.\n$detailMap"); break;
            case 429: log.error("Too Many Requests: Rate limiting.\n$detailMap"); break;
            case 500: log.error("Internal Server Error: Naughty Spotify!\n$detailMap"); break;
            case 502: log.error("Bad Gateway: The server was acting as a gateway or proxy and received an invalid response from the upstream server.\n$detailMap"); break;
            case 503: log.error("Service Unavailable: Naughty Spotify!\n$detailMap"); break;
            default: break;
        }

        resp.json
    }

    def scrubArtistName(artistName) {
        //Replace accented characters with unaccented equivalent
        artistName = Normalizer.normalize(artistName, Normalizer.Form.NFD);
        artistName = artistName.replaceAll("[^\\p{ASCII}]", "");

        artistName
        .replaceAll('"', '\\"') //Escape Quotes
        .replaceAll("'", "") //Remove Single Quotes
        .replaceAll("\\(.*?\\) ?", "") //Remove bracketed text
        .replaceAll("[\\!\\[\\]]", "") //Remove certain characters
        .replaceAll("[\\.\\,\\+\\-\\_]]", "") //Exchange certain characters for spaces
        .replaceAll("\\\\", "") //Remove all backslashes
    }

    def doWithRetry(Closure closure, int maxAttempts = 10) {
        def resp = closure()
        int attempt = 1

        while((!resp.hasProperty("statusCode") || resp.statusCode.value == 429) && attempt <= maxAttempts) {
            int retryAfter = resp.getHeaders()["Retry-After"].first().toInteger()
            sleep(retryAfter * 1000)

            resp = closure
            attempt++
        }

        return resp
    }
}
