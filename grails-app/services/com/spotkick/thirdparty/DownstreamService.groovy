package com.spotkick.thirdparty

import com.spotkick.SpotkickUserConfig
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

@Transactional
class DownstreamService {
    def utilitiesService
    def grailsApplication
    static final RestBuilder rest = new RestBuilder()

    def getConcerts(Map filters, int page=1) {
        def resp = null
        def results = null

        if(filters.containsKey("areaRestriction")) {
            String metroUrl = getMetroEventsUrl(filters.areaRestriction, filters, page)
            resp = rest.get(metroUrl)
            results = resp.json.resultsPage.results.event
        } else {
            String userUrl = getUserEventsUrl(page)
            resp = rest.get(userUrl)
            results = resp.json.resultsPage.results.calendarEntry*.event
        }

        def formattedConcerts = formatConcertsData(results)
        return doFilters(formattedConcerts, filters)
    }

    def getMetroEventsUrl(String locationQuery, filters, page=1) {
        String encodedLocationQuery = URLEncoder.encode(locationQuery)
        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String songkickApiKey = spotkickUserConfig.getSongkickApiKey()

        String locationUrl = "$grailsApplication.config.com.spotkick.songkick.url" +
                "/search/locations.json?" +
                "query=$encodedLocationQuery&" +
                "apikey=$songkickApiKey"

        def locationResp = rest.get(locationUrl)

        String metroAreaId = locationResp.json.resultsPage.results.location.first().metroArea.id

        DateTimeFormatter dtf = DateTimeFormat.forPattern('yyyy-MM-dd')
        "$grailsApplication.config.com.spotkick.songkick.url" +
                "/events.json?" +
                "apikey=$songkickApiKey&" +
                "location=sk:$metroAreaId&" +
                "min_date=${dtf.print(filters.dateRestriction.startDate)}&" +
                "max_date=${dtf.print(filters.dateRestriction.endDate)}&" +
                "page=$page"
    }

    def getUserEventsUrl(page) {
        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String songkickApiKey = spotkickUserConfig.getSongkickApiKey()
        String songkickApiUsername = spotkickUserConfig.getSongkickUsername()

        "$grailsApplication.config.com.spotkick.songkick.url" +
                "/users/$songkickApiUsername/calendar.json?" +
                "reason=tracked_artist&" +
                "apikey=$songkickApiKey&" +
                "page=$page"
    }

    def formatConcertsData(results) {
        def data=[]
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
                            date: event.start.date
                    ],
                    url: event.uri,
                    popularity: event.popularity,
                    type: event.type
            ]
        }

        data
    }

    def doFilters(data, filters) {
        if(filters.empty) { return data }
        def filterByDate = filters.containsKey("dateRestriction")
        def filterByType = filters.containsKey("festivalRestriction")
        def filterByArtist = filters.containsKey("artistRestriction")

        def filteredData = []

        data.each { concert ->
            boolean dateCompliant = true
            boolean typeCompliant = true
            boolean artistCompliant = true

            if(filterByDate) {
                def concertDate = concert.start.date ? new DateTime(concert.start.date) : null
                def startDate = filters.dateRestriction.startDate
                def endDate = filters.dateRestriction.endDate

                if(!concertDate) {
                    log.error("null date for this event: ${concert.url}")
                    return filteredData
                }

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
                def filterArtistsList = filters.artistRestriction
                def concertArtistsList = concert.performances*.name[0]

                if( !filterArtistsList.any { concertArtistsList.contains(it) } ) {
                    artistCompliant = false
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
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy")

        if(params.containsKey("startDate") && params.containsKey("endDate")) {
            filters.dateRestriction = [
                    startDate: new DateTime(dtf.parseDateTime(params.startDate)),
                    endDate  : new DateTime(dtf.parseDateTime(params.endDate)),
            ]
        }

        filters.festivalRestriction = [
                includeFestivals: params.includeFestivals == "on" ? true : false
        ]

        if(params.containsKey("artists")) {
            filters.artistRestriction = []
            if( !(params.artists instanceof ArrayList) ) {
                params.artists = [params.artists].flatten()
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

    def generatePlaylistName(filters) {
        DateTime startDate = filters.dateRestriction.startDate
        DateTime endDate = filters.dateRestriction.endDate
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yy")

        "Spotkick Gigs ${dtf.print(startDate)}-${dtf.print(endDate)}"
    }
}
