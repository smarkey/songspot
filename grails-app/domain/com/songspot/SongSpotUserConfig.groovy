package com.songspot

import org.joda.time.DateTime

class SongSpotUserConfig {

    String songKickApiKey
    String songKickUsername
    String spotifyAuthorizationCode
    String spotifyAccessToken
    String spotifyRefreshToken
    Date spotifyRefreshTokenExpiry
    String spotifyUserId

    static constraints = {
        songKickApiKey nullable:true
        songKickUsername nullable:true
        spotifyAuthorizationCode nullable:true
        spotifyAccessToken nullable:true
        spotifyRefreshToken nullable:true
        spotifyRefreshTokenExpiry nullable:true
        spotifyUserId nullable:true
    }
}
