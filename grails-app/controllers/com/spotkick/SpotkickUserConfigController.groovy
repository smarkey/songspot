package com.spotkick

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SpotkickUserConfigController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond SpotkickUserConfig.list(params), model:[spotkickUserConfigCount: SpotkickUserConfig.count()]
    }

    def show(SpotkickUserConfig spotkickUserConfig) {
        respond spotkickUserConfig
    }

    def create() {
        respond new SpotkickUserConfig(params)
    }

    @Transactional
    def save(SpotkickUserConfig spotkickUserConfig) {
        if (spotkickUserConfig == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (spotkickUserConfig.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond spotkickUserConfig.errors, view:'create'
            return
        }

        spotkickUserConfig.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'spotkickUserConfig.label', default: 'SpotkickUserConfig'), spotkickUserConfig.id])
                redirect spotkickUserConfig
            }
            '*' { respond spotkickUserConfig, [status: CREATED] }
        }
    }

    def edit(SpotkickUserConfig spotkickUserConfig) {
        respond spotkickUserConfig
    }

    @Transactional
    def update(SpotkickUserConfig spotkickUserConfig) {
        if (spotkickUserConfig == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (spotkickUserConfig.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond spotkickUserConfig.errors, view:'edit'
            return
        }

        spotkickUserConfig.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'spotkickUserConfig.label', default: 'SpotkickUserConfig'), spotkickUserConfig.id])
                redirect spotkickUserConfig
            }
            '*'{ respond spotkickUserConfig, [status: OK] }
        }
    }

    @Transactional
    def delete(SpotkickUserConfig spotkickUserConfig) {

        if (spotkickUserConfig == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        spotkickUserConfig.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'spotkickUserConfig.label', default: 'SpotkickUserConfig'), spotkickUserConfig.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'spotkickUserConfig.label', default: 'SpotkickUserConfig'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
