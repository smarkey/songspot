package com.songspot

class MainController {
    def songkickService
    def spotifyService

    def index() {
        render(view:"/index")
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
//        def threadedMethodList = new PromiseList()

        artists.each { artist ->
            def artistId = spotifyService.findArtistByName(artist).id.first()
            def numberOfTracks = params.int("numberOfTracks")
            def topTracks = spotifyService.getArtistsTopTracks(artistId, numberOfTracks)
            def uris = topTracks*.uri

            spotifyService.addTrackToPlaylist(uris, playlistId)

//            threadedMethodList << task {
//                spotifyService.addTrackToPlaylist(uris, playlistId)
//            }
        }

//        threadedMethodList.onComplete { List results ->
//            log.info("Processed ${results.size()} spotify actions")
//        }.onError { Throwable err ->
//            log.error("An error occured with spotify: ${err.message}")
//        }

        flash.message = "Created Playlist '${params.name}' with Top Tracks for ${artists.size()} artists: ${artists.join(", ")}"
        redirect(action: "index")
    }

    def getConcertArtists() {
        def filters = songkickService.formatFilters(params)
        def concertsJson = songkickService.getConcerts(filters)
        render (view:"/songKick/list", model:[artists: concertsJson*.performances*.name.unique().flatten()])
    }

    def getConcerts() {
        def filters = songkickService.formatFilters(params)
        render songkickService.getConcerts(filters)
    }

    def getConcertsByDateRange() {
        def filters = songkickService.formatFilters(params)
        render songkickService.getConcerts(filters)
    }
}
