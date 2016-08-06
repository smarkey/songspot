package com.songspot

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class SongSpotUserSongSpotRole implements Serializable {

	private static final long serialVersionUID = 1

	SongSpotUser songSpotUser
	SongSpotRole songSpotRole

	@Override
	boolean equals(other) {
		if (other instanceof SongSpotUserSongSpotRole) {
			other.songSpotUserId == songSpotUser?.id && other.songSpotRoleId == songSpotRole?.id
		}
	}

	@Override
	int hashCode() {
		def builder = new HashCodeBuilder()
		if (songSpotUser) builder.append(songSpotUser.id)
		if (songSpotRole) builder.append(songSpotRole.id)
		builder.toHashCode()
	}

	static SongSpotUserSongSpotRole get(long songSpotUserId, long songSpotRoleId) {
		criteriaFor(songSpotUserId, songSpotRoleId).get()
	}

	static boolean exists(long songSpotUserId, long songSpotRoleId) {
		criteriaFor(songSpotUserId, songSpotRoleId).count()
	}

	private static DetachedCriteria criteriaFor(long songSpotUserId, long songSpotRoleId) {
		SongSpotUserSongSpotRole.where {
			songSpotUser == SongSpotUser.load(songSpotUserId) &&
			songSpotRole == SongSpotRole.load(songSpotRoleId)
		}
	}

	static SongSpotUserSongSpotRole create(SongSpotUser songSpotUser, SongSpotRole songSpotRole) {
		def instance = new SongSpotUserSongSpotRole(songSpotUser: songSpotUser, songSpotRole: songSpotRole)
		instance.save()
		instance
	}

	static boolean remove(SongSpotUser u, SongSpotRole r) {
		if (u != null && r != null) {
			SongSpotUserSongSpotRole.where { songSpotUser == u && songSpotRole == r }.deleteAll()
		}
	}

	static int removeAll(SongSpotUser u) {
		u == null ? 0 : SongSpotUserSongSpotRole.where { songSpotUser == u }.deleteAll()
	}

	static int removeAll(SongSpotRole r) {
		r == null ? 0 : SongSpotUserSongSpotRole.where { songSpotRole == r }.deleteAll()
	}

	static constraints = {
		songSpotRole validator: { SongSpotRole r, SongSpotUserSongSpotRole ur ->
			if (ur.songSpotUser?.id) {
				SongSpotUserSongSpotRole.withNewSession {
					if (SongSpotUserSongSpotRole.exists(ur.songSpotUser.id, r.id)) {
						return ['userRole.exists']
					}
				}
			}
		}
	}

	static mapping = {
		id composite: ['songSpotUser', 'songSpotRole']
		version false
	}
}
