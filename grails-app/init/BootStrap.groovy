import com.spotkick.SpotkickUser
import com.spotkick.SpotkickRole
import com.spotkick.SpotkickUserConfig
import com.spotkick.SpotkickUserSpotkickRole

class BootStrap {

    def init = { servletContext ->
        createSpotkickRoleIfNecessary("ROLE_USER")
        createSpotkickRoleIfNecessary("ROLE_ADMIN")
        //createSpotkickUserIfNecessary([username:"admin", password:"password"], "ROLE_ADMIN")
        //createSpotkickUserIfNecessary([username:"user", password:"password"], "ROLE_USER")
    }
    
    def destroy = {
    }

    def createSpotkickRoleIfNecessary(String authority) {
        if(!SpotkickRole.findByAuthority(authority)){
            SpotkickRole spotkickRole = new SpotkickRole(authority: authority).save()
            log.info("Spotkick created: $spotkickRole")
        }
    }

    def createSpotkickUserIfNecessary(Map values, String authority) {
        if(!SpotkickUser.findAllByUsername(values.username)){
            SpotkickUserConfig spotkickUserConfig = new SpotkickUserConfig([songkickApiKey:"JFeFSSO2cn7uoIIp", songkickUsername:"steven-markey-1"]).save(flush:true)
            values << [spotkickUserConfig:spotkickUserConfig]

            SpotkickUser spotkickUser = new SpotkickUser(values).save()
            SpotkickRole spotkickRole = SpotkickRole.findByAuthority(authority)
            SpotkickUserSpotkickRole.create(spotkickUser, spotkickRole)

            SpotkickUserSpotkickRole.withSession {
                it.flush()
                it.clear()
            }

            log.info("SpotkickUser created: $spotkickUser with SpotkickRole: $spotkickRole")
        }
    }
}
