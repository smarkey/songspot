package com.songspot

class MainController {
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

        def filters = songkickService.formatFilters(params)
        def artists = songkickService.getConcerts(filters).performances*.name.flatten().unique()

        artists.each { artist ->
            def artistId = spotifyService.findArtistByName(artist).id[0]
            def topTracksUris = spotifyService.getArtistsTopTracks(artistId, params.int("numberOfTracks"))*.uri
            spotifyService.addTrackToPlaylist(topTracksUris, playlistId)
        }

        flash.message = "Created Playlist '$params.name' with Top Tracks for ${artists.size()} artists: ${artists.split()}"
        render(view:"/index_new")
    }

    def getConcertArtists() {
        def concertsJson = songkickService.getConcerts()
        concertsJson.resultsPage.results.calendarEntry*.event.performance.displayName.flatten()
    }

    def getConcerts() {
        render songkickService.getConcerts()
    }

    def getConcertsByDateRange() {
        def filters = songkickService.formatFilters(params)
        render songkickService.getConcerts(filters)
    }
}
