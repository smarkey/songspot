package com.spotkick.thirdparty

import com.spotkick.SpotkickUserConfig
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import org.joda.time.DateTime

@Transactional
class DownstreamService {
    def utilitiesService
    def grailsApplication
    static final RestBuilder rest = new RestBuilder()

    def getConcerts(Map filters) {
        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String songkickApiKey = spotkickUserConfig.getSongkickApiKey()
        String songkickApiUsername = spotkickUserConfig.getSongkickUsername()
        def results = null

        if(filters.containsKey("areaRestriction")) {
            String locationQuery = URLEncoder.encode(filters.areaRestriction)
            String locationUrl = "http://api.songkick.com/api/3.0/search/locations.json?query=$locationQuery&apikey=$songkickApiKey"
            def locationResp = rest.get(locationUrl)
            String metroAreaId = locationResp.json.resultsPage.results.location.first().metroArea.id
            String metroUrl = "http://api.songkick.com/api/3.0/metro_areas/$metroAreaId/calendar.json?apikey=$songkickApiKey"
            def metroResp = rest.get(metroUrl)
            results = metroResp.json.resultsPage.results.event
        } else {
            String url = "$grailsApplication.config.com.spotkick.songkick.url/users/$songkickApiUsername/calendar.json?reason=tracked_artist&apikey=$songkickApiKey"
            def resp = rest.get(url)
            results = resp.json.resultsPage.results.calendarEntry*.event
        }

        def data = []
        results.each { event ->
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
        def filterByArtist = filters.containsKey("artistRestriction")

        def filteredData = []
        data.each { concert ->
            def concertDate = new DateTime(concert.start.datetime)
            boolean dateCompliant = true
            boolean typeCompliant = true
            boolean artistCompliant = true

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

            if(filterByArtist) {
                artistCompliant = false
                def filterArtistsList = filters.artistRestriction
                def concertArtistsList = concert.performances*.name[0]

                if( filterArtistsList.any { concertArtistsList.contains(it) } ) {
                    artistCompliant = true
                }
            }

            if(dateCompliant && typeCompliant && artistCompliant) {
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

        if(params.containsKey("artists")) {
            filters.artistRestriction = []
            if( !(params.artists instanceof ArrayList) ) {
                params.artists = [params.artists]
            }

            params.artists.each {
                filters.artistRestriction << it
            }
        }

        if(params.containsKey("area")) {
            filters.areaRestriction = params.area
        }

        return filters
    }
}
