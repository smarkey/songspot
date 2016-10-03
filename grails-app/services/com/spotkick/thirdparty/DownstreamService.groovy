package com.spotkick.thirdparty

import com.spotkick.SpotkickUserConfig
import com.spotkick.UtilitiesService
import grails.core.GrailsApplication
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

@Transactional
class DownstreamService {
    UtilitiesService utilitiesService
    GrailsApplication grailsApplication
    static final RestBuilder rest = new RestBuilder()

    def getConcerts(Map filters, int page=1) {
        def resp = null
        def results = []

        if(filters.containsKey("areaRestriction")) {
            String metroUrl = getMetroEventsUrl(filters.areaRestriction, filters, page)

            resp = rest.get(metroUrl)
            def resultsPage = resp.json.resultsPage
            results << resultsPage.results.event

            int totalPages = Math.ceil(resultsPage.totalEntries.toInteger()/resultsPage.perPage.toInteger()).toInteger()
            (2..totalPages).each { pageNumber ->
                results << rest.get(getMetroEventsUrl(filters.areaRestriction, filters, pageNumber)).json.resultsPage.results.event
            }
        } else {
            String userUrl = getUserEventsUrl(page, filters.user)

            resp = rest.get(userUrl)
            def resultsPage = resp.json.resultsPage
            results << resultsPage.results.calendarEntry*.event

            int totalPages = Math.ceil(resultsPage.totalEntries.toInteger()/resultsPage.perPage.toInteger()).toInteger()
            (2..totalPages).each { pageNumber ->
                results << rest.get(getUserEventsUrl(page, filters.user)).json.resultsPage.results.calendarEntry*.event
            }
        }

        def formattedConcerts = formatConcertsData(results.flatten())
        return doFilters(formattedConcerts, filters)
    }

    def getFestivals(Map filters, int page=1) {
        def resp = null
        def results = []

        if(filters.containsKey("areaRestriction")) {
            String metroUrl = getMetroEventsUrl(filters.areaRestriction, filters, page)

            resp = rest.get(metroUrl)
            def resultsPage = resp.json.resultsPage
            results << resultsPage.results.event

            int totalPages = Math.ceil(resultsPage.totalEntries.toInteger()/resultsPage.perPage.toInteger()).toInteger()
            (2..totalPages).each { pageNumber ->
                results << rest.get(getMetroEventsUrl(filters.areaRestriction, filters, pageNumber)).json.resultsPage.results.event
            }
        } else {
            String userUrl = getUserEventsUrl(page, filters.user)

            resp = rest.get(userUrl)
            def resultsPage = resp.json.resultsPage
            results << resultsPage.results.calendarEntry*.event

            int totalPages = Math.ceil(resultsPage.totalEntries.toInteger()/resultsPage.perPage.toInteger()).toInteger()
            (2..totalPages).each { pageNumber ->
                results << rest.get(getUserEventsUrl(page, filters.user)).json.resultsPage.results.calendarEntry*.event
            }
        }

        def formattedConcerts = formatFestivalsData(results.flatten())
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

    def getUserEventsUrl(page, username=null) {
        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String songkickApiKey = spotkickUserConfig.getSongkickApiKey()
        String songkickApiUsername = spotkickUserConfig.getSongkickUsername()

        if(username && username!="") {
            songkickApiUsername = username
        }

        "$grailsApplication.config.com.spotkick.songkick.url" +
                "/users/$songkickApiUsername/calendar.json?" +
                "reason=tracked_artist&" +
                "apikey=$songkickApiKey&" +
                "page=$page"
    }

    def formatConcertsData(results) {
        def data=[]
        results.each { event ->
            if (event?.performance && event?.performance.size() > 0) {
                def performances = []
                event.performance.each { performance ->
                    performances << [
                        id  : performance.id,
                        name: performance.displayName
                    ]
                }

                data << [
                    id          : event.id,
                    performances: [
                        performances
                    ],
                    venue       : [
                        id  : event.venue.id,
                        name: event.venue.displayName
                    ],
                    location    : event.location,
                    start       : [
                        date: event.start.date
                    ],
                    url         : event.uri,
                    popularity  : event.popularity,
                    type        : event.type
                ]
            }
        }

        data
    }

    def formatFestivalsData(results) {
        results.findAll { it?.type=="Festival"}
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

        if(params.containsKey("location")) {
            filters.areaRestriction = params.location
        }

        return filters
    }
}
