package com.songspot

class SongKickController {
    def songkickService

    def getConcerts() {
        render songkickService.getConcerts()
    }
}
