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

        List<String> uris = []
        List<String> missingArtists = []

        artists.each { artist ->
            def spotifyArtistSearchResult = upstreamService.findArtistByName(artist)

            if(!spotifyArtistSearchResult || spotifyArtistSearchResult?.size() == 0) {
                missingArtists << artist
                return
            }

            String artistId = spotifyArtistSearchResult.first().id

            def topTracks = upstreamService.getArtistsTopTracks(artistId, numberOfTracks)

            if(topTracks*.uri.empty) {
                log.error("Artist has no tracks in Spotify: $artist")
                return
            }

            uris << topTracks*.uri
        }

        List<String> correctedMissingArtistsList = []
        missingArtists.each { artist ->
            def splitArtists = null

            if(artist.contains(" & ")) {
                splitArtists = artist.split(" & ")
            } else if(artist.contains("&")) {
                splitArtists = artist.split("&")
            } else if(artist.contains(" and ")) {
                splitArtists =  artist.split(" and ")
            }

            if(splitArtists) {
                int foundNothingForSplits = 0

                splitArtists.each { splitMissingArtist ->
                    def spotifyArtistSearchResult = upstreamService.findArtistByName(splitMissingArtist)

                    if(!spotifyArtistSearchResult || spotifyArtistSearchResult?.size() == 0) {
                        foundNothingForSplits++
                        return
                    }

                    String artistId = spotifyArtistSearchResult.first().id

                    def topTracks = upstreamService.getArtistsTopTracks(artistId, numberOfTracks)

                    if(topTracks*.uri.empty) {
                        log.error("Artist has no tracks in Spotify: $artist")
                        return
                    }

                    uris << topTracks*.uri
                    return
                }

                if(foundNothingForSplits == 0) {
                    correctedMissingArtistsList << artist
                }
            } else {
                correctedMissingArtistsList << artist
            }
        }

        def resultJson = upstreamService.addTrackToPlaylist(uris.flatten().unique(), playlistJsonResponse?.id)

        int notMissingArtists = artists.size() - correctedMissingArtistsList.size()

        log.info("Created Playlist '${playlistName}' with $numberOfTracks tracks each for ${notMissingArtists}/${artists.size()} artists\n" +
                "Not Found: ${correctedMissingArtistsList}")
        render (view:"/downstream/playlistPreview", model:[
                spotifyPlaylistUri: playlistJsonResponse?.uri,
                missingArtists: correctedMissingArtistsList
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
                festivals: festivals.collectEntries {
                    if(it.performance?.size() > 0) {
                        [ "${it?.displayName}": it?.performance*.displayName?.unique()?.sort()]
                    } else {
                        [:]
                    }
                }
        ])
    }
}
