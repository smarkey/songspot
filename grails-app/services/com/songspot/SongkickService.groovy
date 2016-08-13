package com.songspot

import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import org.joda.time.DateTime

@Transactional
class SongkickService {
    def utilitiesService
    def grailsApplication
    static final RestBuilder rest = new RestBuilder()

    def getConcerts(Map filters) {
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

        return doFilters(data, filters)
    }

    def doFilters(data, filters) {
        if(filters.empty) { return data }
        def filterByDate = filters.containsKey("dateRestriction")
        def filterByType = filters.containsKey("festivalRestriction")

        def filteredData = []
        data.each { concert ->
            def concertDate = new DateTime(concert.start.datetime)
            boolean dateCompliant = true
            boolean typeCompliant = true

            if(filterByDate) {
                def startDate = filters.dateRestriction.startDate
                def endDate = filters.dateRestriction.endDate

                if(concertDate < startDate || concertDate > endDate) {
                    dateCompliant = false
                }
            }

            if(filterByType) {
                def includeFestivals = filters.festivalRestriction.includeFestivals

                if (!includeFestivals && concert.type == "Festival") {
                    typeCompliant = false
                }
            }

            if(dateCompliant && typeCompliant) {
                filteredData << concert
            }
        }

        return filteredData
    }

    def formatFilters(Map params) {
        Map filters = [:]

        if(params.containsKey("startDate") && params.containsKey("endDate")) {
            filters.dateRestriction = [
                    startDate: new DateTime(params.startDate),
                    endDate  : new DateTime(params.endDate),
            ]
        }

        filters.festivalRestriction = [
                includeFestivals: params.includeFestivals == "on" ? true : false
        ]

        return filters
    }
}
