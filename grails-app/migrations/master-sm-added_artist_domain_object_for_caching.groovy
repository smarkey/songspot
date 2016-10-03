databaseChangeLog = {

    changeSet(author: "steven (generated)", id: "1475003250736-1") {
        createTable(tableName: "artist") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "artistPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "spotify_id", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }
}
