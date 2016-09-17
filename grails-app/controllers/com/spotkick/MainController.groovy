package com.spotkick

class MainController {
    def downstreamService
    def upstreamService

    static final SCRUBBED_CHARS = "[-+!.^:,]"

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
            String scrubbedArtistName = artist.replaceAll(SCRUBBED_CHARS,"")
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

        def notMissingArtists = artists
        notMissingArtists.removeAll(missingArtists)
        log.info("Created Playlist '${playlistName}' with $numberOfTracks tracks each for ${artists.size() - missingArtists?.size()}/${artists.size()} artists\nFound: ${notMissingArtists}\nNot Found: ${missingArtists}")
        render (view:"/downstream/playlistPreview", model:[
                spotifyPlaylistUri: playlistJsonResponse?.uri,
                missingArtists: missingArtists
        ])
    }

    def getConcertArtistsOrFestivals() {
        if(params.getArtistsButton) {
            redirect(action: "getConcertArtists", params: params)
        }
        if(params.getFestivalsButton) {
            redirect(action: "getFestivals", params: params)
        }
    }

    def getConcertArtists() {
        log.info("Getting Concert Artists...")
        def filters = downstreamService.formatFilters(params)
        def concerts = downstreamService.getConcerts(filters)
        def artistNames = concerts*.performances*.name.flatten().unique().sort()
        log.info("Found ${artistNames.size()} artists:\n$artistNames")
        render (view:"/downstream/list", model:[
                artists: artistNames
        ])
    }

    def getFestivals() {
        log.info("Getting Festivals...")
        params.includeFestivals = "on"
        def filters = downstreamService.formatFilters(params)
        def festivals = downstreamService.getFestivals(filters)
        def festivalNames = festivals*.displayName
        log.info("Found ${festivalNames.size()} festivals:\n$festivalNames")
        render (view:"/downstream/festivalsList", model:[
                festivals: festivals.collectEntries { if(it.performance?.size() > 0) { [ "${it.displayName}": it.performance*.displayName.unique().sort()] } }
        ])
    }
}
