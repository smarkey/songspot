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
        file = "${targetDir}/logs/spotkick.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%d{HH:mm:ss.SSS} %level %logger - %msg%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
    logger("grails.app", DEBUG, ['FULL_STACKTRACE'])
    logger("grails.app", INFO, ['FULL_STACKTRACE'])
}

if (Environment.isWarDeployed() && targetDir) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/logs/spotkick.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%d{HH:mm:ss.SSS} %level %logger - %msg%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
    logger("grails.app", INFO, ['FULL_STACKTRACE'], false)
}
