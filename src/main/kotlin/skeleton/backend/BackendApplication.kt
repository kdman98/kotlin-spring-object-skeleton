package skeleton.backend

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
    initDatabase()
    runApplication<BackendApplication>(*args)
}

fun initDatabase() {
    Database.connect(
        url = "jdbc:h2:mem:my_database;DB_CLOSE_DELAY=-1;",
        driver = "org.h2.Driver",
        user = "sa",
        password = ""
    )

    val schemaPath = Paths.get("src/main/resources/schema.sql")
    val schemaSQL = Files.readString(schemaPath)

    transaction {
        exec(schemaSQL)
    }
}