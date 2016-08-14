package com.spotkick

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class SpotkickUserSpotkickRole implements Serializable {

	private static final long serialVersionUID = 1

	SpotkickUser spotkickUser
	SpotkickRole spotkickRole

	@Override
	boolean equals(other) {
		if (other instanceof SpotkickUserSpotkickRole) {
			other.spotkickUserId == spotkickUser?.id && other.spotkickRoleId == spotkickRole?.id
		}
	}

	@Override
	int hashCode() {
		def builder = new HashCodeBuilder()
		if (spotkickUser) builder.append(spotkickUser.id)
		if (spotkickRole) builder.append(spotkickRole.id)
		builder.toHashCode()
	}

	static SpotkickUserSpotkickRole get(long spotkickUserId, long spotkickRoleId) {
		criteriaFor(spotkickUserId, spotkickRoleId).get()
	}

	static boolean exists(long spotkickUserId, long spotkickRoleId) {
		criteriaFor(spotkickUserId, spotkickRoleId).count()
	}

	private static DetachedCriteria criteriaFor(long spotkickUserId, long spotkickRoleId) {
		SpotkickUserSpotkickRole.where {
			spotkickUser == SpotkickUser.load(spotkickUserId) &&
			spotkickRole == SpotkickRole.load(spotkickRoleId)
		}
	}

	static SpotkickUserSpotkickRole create(SpotkickUser spotkickUser, SpotkickRole spotkickRole) {
		def instance = new SpotkickUserSpotkickRole(spotkickUser: spotkickUser, spotkickRole: spotkickRole)
		instance.save()
		instance
	}

	static boolean remove(SpotkickUser u, SpotkickRole r) {
		if (u != null && r != null) {
			SpotkickUserSpotkickRole.where { spotkickUser == u && spotkickRole == r }.deleteAll()
		}
	}

	static int removeAll(SpotkickUser u) {
		u == null ? 0 : SpotkickUserSpotkickRole.where { spotkickUser == u }.deleteAll()
	}

	static int removeAll(SpotkickRole r) {
		r == null ? 0 : SpotkickUserSpotkickRole.where { spotkickRole == r }.deleteAll()
	}

	static constraints = {
		spotkickRole validator: { SpotkickRole r, SpotkickUserSpotkickRole ur ->
			if (ur.spotkickUser?.id) {
				SpotkickUserSpotkickRole.withNewSession {
					if (SpotkickUserSpotkickRole.exists(ur.spotkickUser.id, r.id)) {
						return ['userRole.exists']
					}
				}
			}
		}
	}

	static mapping = {
		id composite: ['spotkickUser', 'spotkickRole']
		version false
	}
}
