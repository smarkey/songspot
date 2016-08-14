package com.spotkick

class MainController {
    def downstreamService
    def upstreamService

    def index() {
        render(view:"/index")
    }

    def addAllConcertArtistsTopTracksToNewPlaylist() {
        flash.message = ""
        def playlistId = upstreamService.createPlaylist(params.name).id

        def filters = downstreamService.formatFilters(params)
        def artists = downstreamService.getConcerts(filters).performances*.name.flatten().unique()

        artists.each { artist ->
            def scrubbedArtistName = artist.replaceAll("[-+!.^:,]","")
            def spotifyArtistSearchResult = upstreamService.findArtistByName(scrubbedArtistName)

            if(spotifyArtistSearchResult.size() < 1) {
                flash.message << "$artist is not on Spotify"
                return
            }

            def artistId = spotifyArtistSearchResult.first().id
            def numberOfTracks = params.int("numberOfTracks")
            def topTracks = upstreamService.getArtistsTopTracks(artistId, numberOfTracks)
            def uris = topTracks*.uri

            upstreamService.addTrackToPlaylist(uris, playlistId)
        }

        log.info("Created Playlist '${params.name}' with Top Tracks for ${artists.size()} artists: ${artists.join(", ")}")
        redirect(action: "index")
    }

    def getConcertArtists() {
        log.info("Getting Concert Artists...")
        def filters = downstreamService.formatFilters(params)
        def concerts = downstreamService.getConcerts(filters)
        def artistNames = concerts*.performances*.name.flatten().unique()
        log.info("Found: $artistNames")
        render (view:"/downstream/list", model:[artists: artistNames])
    }

    def getConcerts() {
        log.info("Getting Concerts...")
        def filters = downstreamService.formatFilters(params)
        def concerts = downstreamService.getConcerts(filters)
        log.info("Found: ${concerts*.displayName.join(", ")}")
        render concerts
    }
}
