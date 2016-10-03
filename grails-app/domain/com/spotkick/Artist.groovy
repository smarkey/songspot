package com.spotkick

class Artist {

    String name
    String spotifyId

    static constraints = {
        name nullable: false
        spotifyId nullable: false
    }
}
