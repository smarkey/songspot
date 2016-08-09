package com.songspot

import grails.transaction.Transactional

@Transactional
class UtilitiesService {
    def springSecurityService

    def getUserConfig() {
        SongSpotUser songSpotUser = springSecurityService.getCurrentUser()
        songSpotUser.getSongSpotUserConfig()
    }
}
