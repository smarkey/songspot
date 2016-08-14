package com.spotkick

class SpotkickUserConfig {

    String songkickApiKey
    String songkickUsername
    String spotifyAuthorizationCode
    String spotifyAccessToken
    String spotifyRefreshToken
    Date spotifyRefreshTokenExpiry
    String spotifyUserId

    static constraints = {
        songkickApiKey nullable:true
        songkickUsername nullable:true
        spotifyAuthorizationCode nullable:true
        spotifyAccessToken nullable:true
        spotifyRefreshToken nullable:true
        spotifyRefreshTokenExpiry nullable:true
        spotifyUserId nullable:true
    }
}
