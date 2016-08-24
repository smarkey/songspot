package com.spotkick

import grails.converters.JSON

class MainController {
    def downstreamService
    def upstreamService

    def index() {
        render(view:"/index")
    }

    def addAllConcertArtistsTopTracksToNewPlaylist() {
        List<String> missingArtists = []
        def filters = downstreamService.formatFilters(params)
        def artists = downstreamService.getConcerts(filters).performances*.name.flatten().unique()
        def resultJson = null

        if(!artists || artists.size() == 0) {
            log.info("No artists specified...")
            flash.message = "No artists specified"
            redirect(action:"index")
        }

        def playlistJsonResponse = upstreamService.createPlaylist(params.name)

        artists.each { artist ->
            String scrubbedArtistName = artist.replaceAll("[-+!.^:,]","")
            def spotifyArtistSearchResult = upstreamService.findArtistByName(scrubbedArtistName)

            if(spotifyArtistSearchResult.size() == 0) {
                missingArtists << artist
                return
            }

            String artistId = spotifyArtistSearchResult.first().id
            int numberOfTracks = params.int("numberOfTracks")
            def topTracks = upstreamService.getArtistsTopTracks(artistId, numberOfTracks)
            def uris = topTracks*.uri

            resultJson = upstreamService.addTrackToPlaylist(uris, playlistJsonResponse?.id)
        }

        log.info("Created Playlist '${params.name}' with Top Tracks for ${artists.size()} artists: ${artists.join(", ")}")
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
        log.info("Found: $artistNames")
        render (view:"/downstream/list", model:[
                artists: artistNames,
                playlistName: downstreamService.generatePlaylistName(filters),
                getConcertArtistsParams: params
        ])
    }

    def ajaxGetConcerts() {
        int page = params.int("page")
        log.info("Getting Concert Artists on page ${page}...")
        def filters = downstreamService.formatFilters(params)
        def concerts = downstreamService.getConcerts(filters, page)
        def artistNames = concerts*.performances*.name.flatten().unique().sort()
        log.info("Found: $artistNames")

        def json = [artistNames: artistNames]
        render json as JSON
    }
}
