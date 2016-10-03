package com.spotkick.thirdparty

import com.spotkick.*
import grails.core.GrailsApplication
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugins.rest.client.RestBuilder
import grails.util.Environment
import org.joda.time.DateTime

class UpstreamService {
    UtilitiesService utilitiesService
    GrailsApplication grailsApplication
    SpringSecurityService springSecurityService

    static final RestBuilder rest = new RestBuilder()

    def getSpotifyUserId() {
        refreshTokenIfNecessary()

        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = spotkickUserConfig.getSpotifyAccessToken()

        def resp = rest.get("$grailsApplication.config.com.spotkick.spotify.url/me") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
        }
        utilitiesService.handleResponse(resp, [method: "getSpotifyUserId"]).id
    }

    def getSpotifyUserEmailAddress(spotifyAccessToken) {
        def resp = rest.get("$grailsApplication.config.com.spotkick.spotify.url/me") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
        }
        utilitiesService.handleResponse(resp, [method: "getSpotifyUserEmailAddress"]).email
    }

    def getToken(spotifyAuthorizationCode) {
        String redirectUri = grailsApplication.config.com.spotkick.spotify."${Environment.isDevelopmentMode() ? 'testCallback' : 'liveCallback'}"
        String clientId = grailsApplication.config.com.spotkick.spotify.clientId
        String clientSecret = grailsApplication.config.com.spotkick.spotify.clientSecret

        def resp = rest.post("https://accounts.spotify.com/api/token?grant_type=authorization_code&code=$spotifyAuthorizationCode&redirect_uri=$redirectUri") {
            header "Content-Type", "application/x-www-form-urlencoded"
            header "Authorization", "Basic ${"$clientId:$clientSecret".bytes.encodeBase64().toString()}"
        }

        def json = utilitiesService.handleResponse(resp, [method: "getToken"])

        [
            spotifyAccessToken: json.access_token,
            spotifyRefreshToken: json.refresh_token,
            spotifyRefreshTokenExpiry: new DateTime().plusSeconds(json."expires_in".toInteger()-1).toDate()
        ]
    }

    def refreshTokenIfNecessary() {
        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String refreshToken = spotkickUserConfig.getSpotifyRefreshToken()
        DateTime refreshTokenExpiry = new DateTime(spotkickUserConfig.spotifyRefreshTokenExpiry)

        if(refreshTokenExpiry < DateTime.now()) {
            def resp = rest.post("https://accounts.spotify.com/api/token?grant_type=refresh_token&refresh_token=$refreshToken") {
                header "Authorization", "Basic ${"$grailsApplication.config.com.spotkick.spotify.clientId:$grailsApplication.config.com.spotkick.spotify.clientSecret".bytes.encodeBase64().toString()}"
                header "Content-Type", "application/x-www-form-urlencoded"
            }

            spotkickUserConfig.spotifyAccessToken = utilitiesService.handleResponse(resp, [method: "refreshTokenIfNecessary"]).access_token
            spotkickUserConfig.save(flush:true)
        }
    }

    def createPlaylist(String name) {
        log.info("Creating Playlist with name '$name'.")
        refreshTokenIfNecessary()

        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = spotkickUserConfig.getSpotifyAccessToken()
        String spotifyUserId = getSpotifyUserId()

        def resp = rest.post("$grailsApplication.config.com.spotkick.spotify.url/users/$spotifyUserId/playlists?name=${name}&public=false") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
            json name:"${name}",public:false
        }

        def json = utilitiesService.handleResponse(resp, [method: "createPlaylist", playlistName: name])
        SpotkickUser currentUser = springSecurityService.getCurrentUser()
        currentUser.numberOfGeneratedPlaylists += 1
        Playlist playlist = new Playlist([name: name, href: json.href, uri: json.uri]).save()
        currentUser.addToPlaylists(playlist).save()
        return json
    }

    def getArtistsTopTracks(String artistId, int numberOftracks = 5) {
        refreshTokenIfNecessary()

        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = spotkickUserConfig.getSpotifyAccessToken()

        def resp = rest.get("$grailsApplication.config.com.spotkick.spotify.url/artists/$artistId/top-tracks?country=GB") {
            header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
            header "Content-Type", "application/json"
        }

        def result = []
        utilitiesService.handleResponse(resp, [method: "getArtistsTopTracks", artistId: artistId]).tracks.eachWithIndex { track, idx ->
            if(idx < numberOftracks) {
                result << [
                        id  : track.id,
                        name: track.name,
                        uri : track.uri
                ]
            }
        }
        result
    }

    def findArtistByName(String artistName) {
        refreshTokenIfNecessary()

        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = spotkickUserConfig.getSpotifyAccessToken()
        String encodedArtistName = URLEncoder.encode(utilitiesService.scrubArtistName(artistName))

        def resp = utilitiesService.doWithRetry({
            rest.get("$grailsApplication.config.com.spotkick.spotify.url/search?q=$encodedArtistName&type=artist&market=GB&limit=1") {
                header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
                header "Content-Type", "application/json"
            }
        })

        utilitiesService.handleResponse(resp, [method: "findArtistByName", artistName: artistName])?.artists?.items
    }

    def addTrackToPlaylist(trackUris, playlistId) {
        log.info("Batch adding Top Tracks to playlist.")
        refreshTokenIfNecessary()

        String spotifyUserId = getSpotifyUserId()
        SpotkickUserConfig spotkickUserConfig = utilitiesService.getUserConfig()
        String spotifyAccessToken = spotkickUserConfig.getSpotifyAccessToken()
        String encodedUris = trackUris.join(",")

        def resp = utilitiesService.doWithRetry({
            rest.post("$grailsApplication.config.com.spotkick.spotify.url/users/$spotifyUserId/playlists/$playlistId/tracks?position=0&uris=$encodedUris") {
                header "Authorization", "Bearer ${spotifyAccessToken.toString()}"
                header "Accept", "application/json"
            }
        })

        utilitiesService.handleResponse(resp, [method: "addTrackToPlaylist", playlistId: playlistId])
    }

    def createSpotkickUserIfNecessary(Map values, String authority) {
        if(!SpotkickUser.findAllByUsername(values.username)){
            SpotkickUserConfig spotkickUserConfig = new SpotkickUserConfig([songkickApiKey: grailsApplication.config.com.spotkick.songkick.apiKey, songkickUsername:"steven-markey-1"]).save(flush:true)
            values << [spotkickUserConfig:spotkickUserConfig, numberOfGeneratedPlaylists:0]

            SpotkickUser spotkickUser = new SpotkickUser(values).save()
            SpotkickRole spotkickRole = SpotkickRole.findByAuthority(authority)
            SpotkickUserSpotkickRole.create(spotkickUser, spotkickRole)

            SpotkickUserSpotkickRole.withSession {
                it.flush()
                it.clear()
            }

            log.info("SpotkickUser created: $spotkickUser with SpotkickRole: $spotkickRole")
        }
    }
}
