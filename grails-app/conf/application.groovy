import org.joda.time.DateTime
import org.joda.time.LocalDate

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.songspot.SongSpotUser'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.songspot.SongSpotUserSongSpotRole'
grails.plugin.springsecurity.authority.className = 'com.songspot.SongSpotRole'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               				access: ['permitAll']],
	[pattern: '/error',          				access: ['permitAll']],
	[pattern: '/index',          				access: ['ROLE_ADMIN']],
	[pattern: '/index.gsp',      				access: ['ROLE_ADMIN']],
	[pattern: '/shutdown',       				access: ['permitAll']],
	[pattern: '/assets/**',      				access: ['permitAll']],
	[pattern: '/**/js/**',       				access: ['permitAll']],
	[pattern: '/**/css/**',      				access: ['permitAll']],
	[pattern: '/**/images/**',   				access: ['permitAll']],
	[pattern: '/**/favicon.ico', 				access: ['permitAll']],
	[pattern: '/songSpotUserConfig/**',         access: ['ROLE_ADMIN']],
	[pattern: '/songKick/**',          			access: ['ROLE_ADMIN']],
	[pattern: '/spotify/**',          			access: ['ROLE_ADMIN']],
	[pattern: '/main/**',          				access: ['ROLE_ADMIN']],
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

com.songspot = [
        spotify: [
				authUrl: "https://accounts.spotify.com/authorize",
				url: "https://api.spotify.com/v1",
				clientId: "6cd61f6ef9ed4635bb4342f9b137a374",
				clientSecret: "c7f7eb3e1db1480abda45610c80c4f9b"
        ],
		songkick: [
				url: "http://api.songkick.com/api/3.0"
		]
]