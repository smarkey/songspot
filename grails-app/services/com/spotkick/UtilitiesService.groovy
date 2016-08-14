package com.spotkick

import grails.transaction.Transactional

@Transactional
class UtilitiesService {
    def springSecurityService

    def getUserConfig() {
        SpotkickUser spotkickUser = springSecurityService.getCurrentUser()
        spotkickUser.getSpotkickUserConfig()
    }
}
