databaseChangeLog = {

    changeSet(author: "steven (generated)", id: "1474120828950-1") {
        addColumn(tableName: "playlist") {
            column(name: "href", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "steven (generated)", id: "1474120828950-2") {
        addColumn(tableName: "playlist") {
            column(name: "name", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "steven (generated)", id: "1474120828950-3") {
        addColumn(tableName: "playlist") {
            column(name: "uri", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "steven (generated)", id: "1474120828950-4") {
        dropColumn(columnName: "playlist_name", tableName: "playlist")
    }

    changeSet(author: "steven (generated)", id: "1474120828950-5") {
        dropColumn(columnName: "playlist_uri", tableName: "playlist")
    }
}
