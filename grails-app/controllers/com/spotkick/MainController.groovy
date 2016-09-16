package com.spotkick

class MainController {
    def downstreamService
    def upstreamService

    def index() {
        render(view:"/index")
    }

    def addAllConcertArtistsTopTracksToNewPlaylist() {
        def filters = downstreamService.formatFilters(params)
        def artists = filters.artistRestriction
        def resultJson = null
        int numberOfTracks = params.int("numberOfTracks")
        String playlistName = params.name

        if(!artists || artists?.size() == 0) {
            log.error("No artists selected")
            flash.message = "No artists were selected."
            redirect(action:"index")
            return
        }

        if(!playlistName || playlistName == "") {
            log.error("No playlist name")
            flash.message = "No playlist name was provided."
            redirect(action:"index")
            return
        }

        def playlistJsonResponse = upstreamService.createPlaylist(playlistName)

        List<String> missingArtists = []

        artists.each { artist ->
            String scrubbedArtistName = artist.replaceAll("[-+!.^:,]","")
            def spotifyArtistSearchResult = upstreamService.findArtistByName(scrubbedArtistName)

            if(!spotifyArtistSearchResult || spotifyArtistSearchResult?.size() == 0) {
                missingArtists << artist
                return
            }

            String artistId = spotifyArtistSearchResult.first().id

            def topTracks = upstreamService.getArtistsTopTracks(artistId, numberOfTracks)
            def uris = topTracks*.uri

            resultJson = upstreamService.addTrackToPlaylist(uris, playlistJsonResponse?.id)
        }

        log.info("Created Playlist '${playlistName}' with $numberOfTracks tracks each for ${artists.size()} artists:\n${artists}")
        render (view:"/downstream/playlistPreview", model:[
                spotifyPlaylistUri: playlistJsonResponse?.uri,
                missingArtists: missingArtists,
                addAllConcertArtistsTopTracksToNewPlaylistParams: params,
                getConcertArtistsParams: params.getConcertArtistsParams
        ])
    }

    def getConcertArtists() {
        log.info("Getting Concert Artists...")
        def filters = downstreamService.formatFilters(params)
        def concerts = downstreamService.getConcerts(filters)
        def artistNames = concerts*.performances*.name.flatten().unique().sort()
        log.info("Found ${artistNames.size()} artists:\n$artistNames")
        render (view:"/downstream/list", model:[
                artists: artistNames,
                playlistName: downstreamService.generatePlaylistName(filters),
                getConcertArtistsParams: params
        ])
    }
}
