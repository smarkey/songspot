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

        def data = []
        resp.json.resultsPage.results.calendarEntry*.event.each { event ->
            def performances = []
            event.performance.each { performance ->
                performances << [
                        id: performance.id,
                        name: performance.displayName
                ]
            }

            data << [
                    id: event.id,
                    performances: [
                            performances
                    ],
                    venue: [
                            id: event.venue.id,
                            name: event.venue.displayName
                    ],
                    location: event.location,
                    start: [
                            datetime: event.start.datetime
                    ],
                    url: event.uri,
                    popularity: event.popularity,
                    type: event.type
            ]
        }
        return data
    }
}
