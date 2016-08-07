import com.songspot.SongSpotRole
import com.songspot.SongSpotUser
import com.songspot.SongSpotUserConfig
import com.songspot.SongSpotUserSongSpotRole

class BootStrap {

    def init = { servletContext ->
        createSongSpotRoleIfNecessary("ROLE_USER")
        createSongSpotRoleIfNecessary("ROLE_ADMIN")
        createSongSpotUserIfNecessary([username:"admin", password:"password"], "ROLE_ADMIN")
        createSongSpotUserIfNecessary([username:"user", password:"password"], "ROLE_USER")
    }
    
    def destroy = {
    }

    def createSongSpotRoleIfNecessary(String authority) {
        if(!SongSpotRole.findByAuthority(authority)){
            SongSpotRole songSpotRole = new SongSpotRole(authority: authority).save()
            log.info("SongSpot created: $songSpotRole")
        }
    }

    def createSongSpotUserIfNecessary(Map values, String authority) {
        if(!SongSpotUser.findAllByUsername(values.username)){
            SongSpotUserConfig songSpotUserConfig = new SongSpotUserConfig([songKickApiKey:"JFeFSSO2cn7uoIIp", songKickUsername:"steven-markey-1"]).save(flush:true)
            values << [songSpotUserConfig:songSpotUserConfig]

            SongSpotUser songSpotUser = new SongSpotUser(values).save()
            SongSpotRole songSpotRole = SongSpotRole.findByAuthority(authority)
            SongSpotUserSongSpotRole.create(songSpotUser, songSpotRole)

            SongSpotUserSongSpotRole.withSession {
                it.flush()
                it.clear()
            }

            log.info("SongSpotUser created: $songSpotUser with SongSpotRole: $songSpotRole")
        }
    }
}
