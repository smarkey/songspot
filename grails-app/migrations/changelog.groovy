databaseChangeLog = {

    changeSet(author: "steven (generated)", id: "1473793213475-1") {
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

    changeSet(author: "steven (generated)", id: "1473793213475-2") {
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

    changeSet(author: "steven (generated)", id: "1473793213475-3") {
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

    changeSet(author: "steven (generated)", id: "1473793213475-4") {
        createTable(tableName: "spotkick_user_spotkick_role") {
            column(name: "spotkick_user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "spotkick_role_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "steven (generated)", id: "1473793213475-5") {
        addPrimaryKey(columnNames: "spotkick_user_id, spotkick_role_id", constraintName: "spotkick_user_spotkick_rolePK", tableName: "spotkick_user_spotkick_role")
    }

    changeSet(author: "steven (generated)", id: "1473793213475-6") {
        addUniqueConstraint(columnNames: "authority", tableName: "spotkick_role")
    }

    changeSet(author: "steven (generated)", id: "1473793213475-7") {
        addUniqueConstraint(columnNames: "username", tableName: "spotkick_user")
    }

    changeSet(author: "steven (generated)", id: "1473793213475-8") {
        addForeignKeyConstraint(baseColumnNames: "spotkick_user_id", baseTableName: "spotkick_user_spotkick_role", constraintName: "FK_8ylq10jyxgn3m4bvcsgkb8esv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "spotkick_user")
    }

    changeSet(author: "steven (generated)", id: "1473793213475-9") {
        addForeignKeyConstraint(baseColumnNames: "spotkick_user_config_id", baseTableName: "spotkick_user", constraintName: "FK_9ss2hb13h93g76xk98lnigf7c", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "spotkick_user_config")
    }

    changeSet(author: "steven (generated)", id: "1473793213475-10") {
        addForeignKeyConstraint(baseColumnNames: "spotkick_role_id", baseTableName: "spotkick_user_spotkick_role", constraintName: "FK_peyh0oif8ujhx2qy6lddnlsxy", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "spotkick_role")
    }
}
