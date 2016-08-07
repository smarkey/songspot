package com.songspot

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SongSpotUserConfigController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond SongSpotUserConfig.list(params), model:[songSpotUserConfigCount: SongSpotUserConfig.count()]
    }

    def show(SongSpotUserConfig songSpotUserConfig) {
        respond songSpotUserConfig
    }

    def create() {
        respond new SongSpotUserConfig(params)
    }

    @Transactional
    def save(SongSpotUserConfig songSpotUserConfig) {
        if (songSpotUserConfig == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (songSpotUserConfig.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond songSpotUserConfig.errors, view:'create'
            return
        }

        songSpotUserConfig.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'songSpotUserConfig.label', default: 'SongSpotUserConfig'), songSpotUserConfig.id])
                redirect songSpotUserConfig
            }
            '*' { respond songSpotUserConfig, [status: CREATED] }
        }
    }

    def edit(SongSpotUserConfig songSpotUserConfig) {
        respond songSpotUserConfig
    }

    @Transactional
    def update(SongSpotUserConfig songSpotUserConfig) {
        if (songSpotUserConfig == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (songSpotUserConfig.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond songSpotUserConfig.errors, view:'edit'
            return
        }

        songSpotUserConfig.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'songSpotUserConfig.label', default: 'SongSpotUserConfig'), songSpotUserConfig.id])
                redirect songSpotUserConfig
            }
            '*'{ respond songSpotUserConfig, [status: OK] }
        }
    }

    @Transactional
    def delete(SongSpotUserConfig songSpotUserConfig) {

        if (songSpotUserConfig == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        songSpotUserConfig.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'songSpotUserConfig.label', default: 'SongSpotUserConfig'), songSpotUserConfig.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'songSpotUserConfig.label', default: 'SongSpotUserConfig'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
