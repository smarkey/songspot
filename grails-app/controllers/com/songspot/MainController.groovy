package com.songspot

class MainController {
    def songkickService
    def spotifyService

    def index() {
        render(view:"/index")
    }

    def addAllConcertArtistsTopTracksToNewPlaylist() {
        flash.message = ""
        def playlistId = spotifyService.createPlaylist(params.name).id

        def filters = songkickService.formatFilters(params)
        def artists = songkickService.getConcerts(filters).performances*.name.flatten().unique()

        artists.each { artist ->
            def scrubbedArtistName = artist.replaceAll("[-+!.^:,]","")
            def spotifyArtistSearchResult = spotifyService.findArtistByName(scrubbedArtistName)

            if(spotifyArtistSearchResult.size() < 1) {
                flash.message << "$artist is not on Spotify"
                return
            }

            def artistId = spotifyArtistSearchResult.first().id
            def numberOfTracks = params.int("numberOfTracks")
            def topTracks = spotifyService.getArtistsTopTracks(artistId, numberOfTracks)
            def uris = topTracks*.uri

            spotifyService.addTrackToPlaylist(uris, playlistId)
        }

        log.info("Created Playlist '${params.name}' with Top Tracks for ${artists.size()} artists: ${artists.join(", ")}")
        redirect(action: "index")
    }

    def getConcertArtists() {
        log.info("Getting Concert Artists...")
        def filters = songkickService.formatFilters(params)
        def concertsJson = songkickService.getConcerts(filters)
        def artistNames = concertsJson*.performances*.name.flatten().unique()
        log.info("Found: $artistNames")
        render (view:"/songKick/list", model:[artists: artistNames])
    }

    def getConcerts() {
        log.info("Getting Concerts...")
        def filters = songkickService.formatFilters(params)
        def concerts = songkickService.getConcerts(filters)
        log.info("Found: ${concerts*.displayName.join(", ")}")
        render concerts
    }
}
