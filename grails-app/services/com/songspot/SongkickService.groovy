package com.songspot

import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional

@Transactional
class SongkickService {
    def utilitiesService
    def grailsApplication
    static final RestBuilder rest = new RestBuilder()

    def getConcerts() {
        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        String songKickApiKey = songSpotUserConfig.getSongKickApiKey()
        String songKickApiUsername = songSpotUserConfig.getSongKickUsername()

        String url = "$grailsApplication.config.com.songspot.songkick.url/users/$songKickApiUsername/calendar.json?reason=tracked_artist&apikey=$songKickApiKey"

        def resp = rest.get(url)
        return resp.json
    }
}
