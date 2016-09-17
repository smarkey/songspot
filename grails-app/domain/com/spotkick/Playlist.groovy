package com.spotkick

class Playlist {

    String name = ""
    String href = ""
    String uri = ""

    static constraints = {
        name nullable: false
        href nullable: false
        uri nullable: false
    }
}
