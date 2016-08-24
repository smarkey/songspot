databaseChangeLog = {

    changeSet(author: "steven (generated)", id: "1472031442749-1") {
        createTable(tableName: "spotkick_role") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "authority", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "steven (generated)", id: "1472031442749-2") {
        createTable(tableName: "spotkick_user") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "account_expired", type: "BIT(1)") {
                constraints(nullable: "false")
            }

            column(name: "account_locked", type: "BIT(1)") {
                constraints(nullable: "false")
            }

            column(name: "enabled", type: "BIT(1)") {
                constraints(nullable: "false")
            }

            column(name: "password", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "password_expired", type: "BIT(1)") {
                constraints(nullable: "false")
            }

            column(name: "spotkick_user_config_id", type: "BIGINT")

            column(name: "username", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "steven (generated)", id: "1472031442749-3") {
        createTable(tableName: "spotkick_user_config") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "songkick_api_key", type: "VARCHAR(255)")

            column(name: "songkick_username", type: "VARCHAR(255)")

            column(name: "spotify_access_token", type: "VARCHAR(255)")

            column(name: "spotify_authorization_code", type: "VARCHAR(255)")

            column(name: "spotify_refresh_token", type: "VARCHAR(255)")

            column(name: "spotify_refresh_token_expiry", type: "datetime")

            column(name: "spotify_user_id", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "steven (generated)", id: "1472031442749-4") {
        createTable(tableName: "spotkick_user_spotkick_role") {
            column(name: "spotkick_user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "spotkick_role_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "steven (generated)", id: "1472031442749-5") {
        addPrimaryKey(columnNames: "spotkick_user_id, spotkick_role_id", constraintName: "PRIMARY", tableName: "spotkick_user_spotkick_role")
    }

    changeSet(author: "steven (generated)", id: "1472031442749-6") {
        addUniqueConstraint(columnNames: "authority", constraintName: "UK_pcugf02rqskrsgpomqoojl60x", tableName: "spotkick_role")
    }

    changeSet(author: "steven (generated)", id: "1472031442749-7") {
        addUniqueConstraint(columnNames: "username", constraintName: "UK_sjde37u641y88eoswpk3i4chi", tableName: "spotkick_user")
    }

    changeSet(author: "steven (generated)", id: "1472031442749-8") {
        createIndex(indexName: "FK_9ss2hb13h93g76xk98lnigf7c", tableName: "spotkick_user") {
            column(name: "spotkick_user_config_id")
        }
    }

    changeSet(author: "steven (generated)", id: "1472031442749-9") {
        createIndex(indexName: "FK_peyh0oif8ujhx2qy6lddnlsxy", tableName: "spotkick_user_spotkick_role") {
            column(name: "spotkick_role_id")
        }
    }

    changeSet(author: "steven (generated)", id: "1472031442749-10") {
        addForeignKeyConstraint(baseColumnNames: "spotkick_user_id", baseTableName: "spotkick_user_spotkick_role", constraintName: "FK_8ylq10jyxgn3m4bvcsgkb8esv", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "spotkick_user")
    }

    changeSet(author: "steven (generated)", id: "1472031442749-11") {
        addForeignKeyConstraint(baseColumnNames: "spotkick_user_config_id", baseTableName: "spotkick_user", constraintName: "FK_9ss2hb13h93g76xk98lnigf7c", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "spotkick_user_config")
    }

    changeSet(author: "steven (generated)", id: "1472031442749-12") {
        addForeignKeyConstraint(baseColumnNames: "spotkick_role_id", baseTableName: "spotkick_user_spotkick_role", constraintName: "FK_peyh0oif8ujhx2qy6lddnlsxy", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "spotkick_role")
    }
}
