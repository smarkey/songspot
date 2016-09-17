databaseChangeLog = {

    changeSet(author: "steven (generated)", id: "1474114078579-1") {
        createTable(tableName: "playlist") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "playlistPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "playlist_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "playlist_uri", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "steven (generated)", id: "1474114078579-2") {
        createTable(tableName: "spotkick_role") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "spotkick_rolePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "authority", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "steven (generated)", id: "1474114078579-3") {
        createTable(tableName: "spotkick_user") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "spotkick_userPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "account_expired", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "account_locked", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "enabled", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "number_of_generated_playlists", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "password", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "password_expired", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "spotkick_user_config_id", type: "BIGINT")

            column(name: "username", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "steven (generated)", id: "1474114078579-4") {
        createTable(tableName: "spotkick_user_config") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "spotkick_user_configPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "songkick_api_key", type: "VARCHAR(255)")

            column(name: "songkick_username", type: "VARCHAR(255)")

            column(name: "spotify_access_token", type: "VARCHAR(255)")

            column(name: "spotify_authorization_code", type: "CLOB")

            column(name: "spotify_refresh_token", type: "VARCHAR(255)")

            column(name: "spotify_refresh_token_expiry", type: "datetime")

            column(name: "spotify_user_id", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "steven (generated)", id: "1474114078579-5") {
        createTable(tableName: "spotkick_user_playlist") {
            column(name: "spotkick_user_playlists_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "playlist_id", type: "BIGINT")
        }
    }

    changeSet(author: "steven (generated)", id: "1474114078579-6") {
        createTable(tableName: "spotkick_user_spotkick_role") {
            column(name: "spotkick_user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "spotkick_role_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "steven (generated)", id: "1474114078579-7") {
        addPrimaryKey(columnNames: "spotkick_user_id, spotkick_role_id", constraintName: "spotkick_user_spotkick_rolePK", tableName: "spotkick_user_spotkick_role")
    }

    changeSet(author: "steven (generated)", id: "1474114078579-8") {
        addUniqueConstraint(columnNames: "authority", constraintName: "UC_SPOTKICK_ROLEAUTHORITY_COL", tableName: "spotkick_role")
    }

    changeSet(author: "steven (generated)", id: "1474114078579-9") {
        addUniqueConstraint(columnNames: "username", constraintName: "UC_SPOTKICK_USERUSERNAME_COL", tableName: "spotkick_user")
    }

    changeSet(author: "steven (generated)", id: "1474114078579-10") {
        addForeignKeyConstraint(baseColumnNames: "playlist_id", baseTableName: "spotkick_user_playlist", constraintName: "FK_5n673eplj7o4w4xrygh9yquph", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "playlist")
    }

    changeSet(author: "steven (generated)", id: "1474114078579-11") {
        addForeignKeyConstraint(baseColumnNames: "spotkick_user_id", baseTableName: "spotkick_user_spotkick_role", constraintName: "FK_8ylq10jyxgn3m4bvcsgkb8esv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "spotkick_user")
    }

    changeSet(author: "steven (generated)", id: "1474114078579-12") {
        addForeignKeyConstraint(baseColumnNames: "spotkick_user_config_id", baseTableName: "spotkick_user", constraintName: "FK_9ss2hb13h93g76xk98lnigf7c", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "spotkick_user_config")
    }

    changeSet(author: "steven (generated)", id: "1474114078579-13") {
        addForeignKeyConstraint(baseColumnNames: "spotkick_user_playlists_id", baseTableName: "spotkick_user_playlist", constraintName: "FK_nwk1jvpffg4myk3ywjlq2tgbn", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "spotkick_user")
    }

    changeSet(author: "steven (generated)", id: "1474114078579-14") {
        addForeignKeyConstraint(baseColumnNames: "spotkick_role_id", baseTableName: "spotkick_user_spotkick_role", constraintName: "FK_peyh0oif8ujhx2qy6lddnlsxy", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "spotkick_role")
    }
}
