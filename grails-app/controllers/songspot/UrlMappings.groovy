package songspot

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:"spotify", action:"authorize")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
