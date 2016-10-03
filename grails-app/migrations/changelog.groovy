databaseChangeLog = {

    include file: 'master-sm-init.groovy'
    include file: 'master-sm-added_playlist_property_changes.groovy'
    include file: 'master-sm-added_artist_domain_object_for_caching.groovy'
}
