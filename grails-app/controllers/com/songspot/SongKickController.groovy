package com.songspot

import grails.plugins.rest.client.RestBuilder

class SongKickController {
    def utilitiesService

    static final String rootUrl = "http://api.songkick.com/api/3.0"

    def index() { }

    def getConcerts() {
        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()

        String songKickApiKey = songSpotUserConfig.getSongKickApiKey()
        String songKickApiUsername = songSpotUserConfig.getSongKickUsername()

        String url = "$rootUrl/users/$songKickApiUsername/calendar.json?reason=tracked_artist&apikey=$songKickApiKey"

        RestBuilder rest = new RestBuilder()
        def resp = rest.get(url)
        render resp.json
    }
}
