// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.spotkick.SpotkickUser'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.spotkick.SpotkickUserSpotkickRole'
grails.plugin.springsecurity.authority.className = 'com.spotkick.SpotkickRole'
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
	[pattern: '/spotkickUserConfig/**',         access: ['ROLE_ADMIN']],
	[pattern: '/downstream/**',          		access: ['ROLE_ADMIN']],
	[pattern: '/upstream/**',          			access: ['ROLE_ADMIN']],
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

com.spotkick = [
        spotify: [
				authUrl: "https://accounts.spotify.com/authorize",
				url: "https://api.spotify.com/v1",
				clientId: "6cd61f6ef9ed4635bb4342f9b137a374",
				clientSecret: "c7f7eb3e1db1480abda45610c80c4f9b",
                testCallback: "http%3A%2F%2Flocalhost%3A8080%2Fupstream%2Fcallback",
				liveCallback: "http%3A%2F%2Fspotkick%2Estevenmarkey%2Ecom%2Fupstream%2Fcallback"
        ],
		songkick: [
				url: "http://api.songkick.com/api/3.0",
				areas: [
						"London UK",
						"Birmingham UK",
						"Liverpool UK",
						"Edinburgh UK",
						"Cardiff UK",
						"Belfast UK",
						"Dublin Ireland"
				]
		]
]

grails.plugin.databasemigration.reports.updateOntart = true
grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']