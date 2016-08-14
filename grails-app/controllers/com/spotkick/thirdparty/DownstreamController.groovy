package com.spotkick.thirdparty

class DownstreamController {
    def downstreamService

    def getConcerts() {
        render downstreamService.getConcerts()
    }
}
