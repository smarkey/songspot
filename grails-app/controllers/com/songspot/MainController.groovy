package com.songspot

import org.joda.time.DateTime

class MainController {
    def utilitiesService
    def songkickService
    def spotifyService

    def test() {
        log.debug("Fetching Songkick concerts")
        render getConcerts()
    }

    def addTrackToPlaylistTest() {
        def artistId = spotifyService.findArtistByName("Aslan").id[0]
        def topTracksUris = spotifyService.getArtistsTopTracks(artistId)*.uri
        def playlistId = spotifyService.createPlaylist("AslanPlaylist").id
        render spotifyService.addTrackToPlaylist(topTracksUris, playlistId)
    }

    def addAllConcertArtistsTopTracksToNewPlaylist() {
        def playlistId = spotifyService.createPlaylist(params.name).id

        def artists = songkickService.getConcerts().resultsPage.results.calendarEntry*.event.performance.displayName.flatten()

        artists.each { artist ->
            def artistId = spotifyService.findArtistByName(artist).id[0]
            def topTracksUris = spotifyService.getArtistsTopTracks(artistId)*.uri
            spotifyService.addTrackToPlaylist(topTracksUris, playlistId)
        }
    }

    def getConcertArtists() {
        def concertsJson = songkickService.getConcerts()
        concertsJson.resultsPage.results.calendarEntry*.event.performance.displayName.flatten()
    }

    def getConcerts() {
        render songkickService.getConcerts()

    }

    def getConcertsByDateRange() {
        def concertsJson = songkickService.getConcerts()
        def startDate = new DateTime(params.startDate)
        def endDate = new DateTime(params.endDate)
        def includeFestivals = params.boolean("includeFestivals")

        def data = []
        concertsJson.each { concert ->
            def concertDate = new DateTime(concert.start.datetime)
            boolean include = false

            if(concertDate > startDate && concertDate < endDate) {
                if(includeFestivals && concert.type == "Festival") {
                    include = true
                } else {
                    include = false
                }
            }

            if(include) {
                data << concert
            }
        }
        render data
    }
}
