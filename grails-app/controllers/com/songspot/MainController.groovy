package com.songspot

class MainController {
    def utilitiesService
    def songkickService
    def spotifyService

    def test() {
        log.debug("Fetching Songkick concerts")
        render getConcerts()
    }

    def addTrackToPlaylistTest() {
        def artistId = spotifyService.findArtistByName("Aslan").artists.items.id[0]
        def topTracksUris = spotifyService.getArtistsTopTracks(artistId)*.uri
        def playlistId = spotifyService.createPlaylist("AslanPlaylist").id
        render spotifyService.addTrackToPlaylist(topTracksUris, playlistId)
    }

    def getConcertArtists() {
        def concertsJson = songkickService.getConcerts()
        concertsJson.resultsPage.results.calendarEntry*.event.performance.displayName.flatten()
    }

    def getConcerts() {
        def concertsJson = songkickService.getConcerts()
        def data = []
        concertsJson.resultsPage.results.calendarEntry*.event.each { event ->
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
                    popularity: event.popularity
            ]
        }
        data
    }
}
