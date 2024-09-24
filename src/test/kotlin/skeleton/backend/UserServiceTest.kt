package skeleton.backend

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import skeleton.backend.config.BadRequestException
import skeleton.backend.enums.UserTypeEnum
import skeleton.backend.exposed.domain.User
import skeleton.backend.repository.UserRepository
import skeleton.backend.service.UserService
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.BeforeTest

class UserServiceTest {
    private val databaseConnection = Database.connect(
        url = "jdbc:h2:mem:my_database;DB_CLOSE_DELAY=-1;",
        driver = "org.h2.Driver",
        user = "sa",
        password = ""
    )

    @BeforeTest
    fun databaseInit() {
        val schemaPath = Paths.get("src/main/resources/schema.sql")
        val schemaSQL = Files.readString(schemaPath)

        transaction {
            exec(schemaSQL)
        }
    }

    @AfterEach
    fun databaseClear() {
        transaction {
            val tables = databaseConnection.dialect.allTablesNames()
            exec("SET REFERENTIAL_INTEGRITY FALSE;")
            tables
                .forEach { table ->
                    exec("TRUNCATE TABLE $table;")
                }
            exec("SET REFERENTIAL_INTEGRITY TRUE;")
        }
    }

    @Test
    fun `signup fails with bad email`() {
        val user = User(
            userId = "user123",
            password = "rawPassword",
            name = "James Abraham Linda Black Sheep Wall",
            userType = UserTypeEnum.PERSONAL,
            idValue = "981118-1012345"
        )

        shouldThrow<BadRequestException> {
            UserService.signup(user)
        }
    }

    @Test
    fun `should hash password and encrypt idValue on signup`() {
        val user = User(
            userId = "user123@dodo.com",
            password = "rawPassword",
            name = "Small Happiness",
            userType = UserTypeEnum.PERSONAL,
            idValue = "981118-1012345"
        )

        UserService.signup(user)

        val insertedUser = UserRepository.findUserByUserId(user.userId)
        skeleton.backend.util.EncryptionUtil.verifyPassword("rawPassword", insertedUser.password) shouldBe true
        insertedUser.idValue shouldBe skeleton.backend.util.EncryptionUtil.encrypt("981118-1012345")
    }

    @Test
    fun `should throw appropriate exception if password is incorrect`() {
        val user = User(
            userId = "asdf@never.com",
            password = "rawPassword",
            name = "Ever Lasting",
            userType = UserTypeEnum.PERSONAL,
            idValue = "981118-1012345"
        )

        UserService.signup(user)

        val exception = shouldThrow<BadRequestException> {
            UserService.processLoginAndReturnToken(user.userId, "wrongPassword")
        }

        exception.message shouldBe "wrong parameter"
    }
}
