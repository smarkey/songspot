import grails.util.BuildSettings
import grails.util.Environment

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} %level %logger - %msg%n"
    }
}

root(ERROR, ['STDOUT'])

def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() && targetDir) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%d{HH:mm:ss.SSS} %level %logger - %msg%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
    logger 'grails.app.controllers', INFO, ['FULL_STACKTRACE']
    logger 'grails.app.services', INFO, ['FULL_STACKTRACE']
    logger 'grails.app.controllers', DEBUG, ['FULL_STACKTRACE']
    logger 'grails.app.services', DEBUG, ['FULL_STACKTRACE']
}
