package com.spotkick

class MainController {
    def downstreamService
    def upstreamService

    def index() {
        render(view:"/index")
    }

    def addAllConcertArtistsTopTracksToNewPlaylist() {
        flash.message = ""
        def playlistJsonResponse = upstreamService.createPlaylist(params.name)
        def filters = downstreamService.formatFilters(params)
        def artists = downstreamService.getConcerts(filters).performances*.name.flatten().unique()
        def resultJson = null

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

            resultJson = upstreamService.addTrackToPlaylist(uris, playlistJsonResponse?.id)
        }

        log.info("Created Playlist '${params.name}' with Top Tracks for ${artists.size()} artists: ${artists.join(", ")}")
        render (view:"/downstream/playlistPreview", model:[spotifyPlaylistUri:playlistJsonResponse?.uri])
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
