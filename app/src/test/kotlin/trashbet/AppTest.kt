package trashbet

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.test.*

class AppTest {

    private var bet1Id: UUID = UUID.randomUUID()
    private var bet2Id: UUID = UUID.randomUUID()
    private var bet3Id: UUID = UUID.randomUUID()
    private var bet4Id: UUID = UUID.randomUUID()
    private var user1Id: UUID = UUID.randomUUID()
    private var user2Id: UUID = UUID.randomUUID()
    private val basicAuth1 = "am9lbDpqb2Vs"
    private val basicAuth2 = "amFjazpqYWNr"

    @Test
    fun testHealthCheck() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/health")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun testUsers() = withTestApplication(Application::main) {
        // get all users
        with(handleRequest(HttpMethod.Get, "/user", setup={addHeader("Authorization", "Basic $basicAuth1")})) {
            assertNotNull(response.content)
            val users = Json.decodeFromString<List<User>>(response.content ?: "")
            assertEquals(2, users.size)
        }
        // get current user
        with(handleRequest(HttpMethod.Get, "/user/me", setup={addHeader("Authorization", "Basic $basicAuth1")})) {
            val user = Json.decodeFromString<User>(response.content ?: "")
            assertEquals(user.name, "joel")
        }
        // promote a user
        with(handleRequest(HttpMethod.Post, "/user/promote?userId=$user2Id", setup={addHeader("Authorization", "Basic $basicAuth1")})) {
            assertEquals(HttpStatusCode.Accepted, response.status())
        }
        // confirm promotion
        with(handleRequest(HttpMethod.Get, "/user/me", setup={addHeader("Authorization", "Basic $basicAuth2")})) {
            val user = Json.decodeFromString<User>(response.content ?: "")
            assertTrue(user.admin)
        }
    }

    @Test
    fun testRegistration() = withTestApplication(Application::main) {
        // make a user
        with(handleRequest(HttpMethod.Post, "/register", setup = {
            setBody("{\"username\":\"test\", \"password\":\"test\"}")
            addHeader("Content-Type", "application/json")
        })) {
            assertNotNull(response.content)
            val user = Json.decodeFromString<User>(response.content ?: "")
            assertNotNull(user.amount)
            assertNotNull(user.id)
            assertFalse(user.admin)
        }
        Unit
    }

    @Test
    fun testBets() = withTestApplication(Application::main) {
        // create new bet
        with(handleRequest(HttpMethod.Post, "/bet", setup = {
            setBody(Json.encodeToString(Bet(description = "test bet", complete = false)))
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertNotNull(response.content)
            assertEquals(HttpStatusCode.Created, response.status())
            val bet = Json.decodeFromString<Bet>(response.content ?: "")
            assertNotNull(bet.id)
            assertFalse(bet.complete)
        }
        // create bet as non-admin
        with(handleRequest(HttpMethod.Post, "/bet", setup = {
            setBody(Json.encodeToString(Bet(description = "test bet", complete = false)))
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic $basicAuth2")
        })) {
            assertEquals(HttpStatusCode.Unauthorized, response.status())
        }
        // get all bets
        with(handleRequest(HttpMethod.Get, "/bet", setup = {
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertNotNull(response.content)
            assertEquals(HttpStatusCode.OK, response.status())
            val bets = Json.decodeFromString<List<Bet>>(response.content ?: "")
            assertEquals(5, bets.size)
        }
        // get only complete bets
        with(handleRequest(HttpMethod.Get, "/bet?complete=true", setup = {
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertNotNull(response.content)
            assertEquals(HttpStatusCode.OK, response.status())
            val bets = Json.decodeFromString<List<Bet>>(response.content ?: "")
            assertEquals(1, bets.size)
        }
    }

    @Test
    fun testCreateWagers() = withTestApplication(Application::main) {
        // wager amount higher than user's amount
        with(handleRequest(HttpMethod.Post, "/bet/$bet1Id/wager", setup = {
            setBody(Json.encodeToString(Wager(amount = 115, outcome = false, userId = UUID.randomUUID(), betId = bet1Id)))
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
        // 0 amount wager
        with(handleRequest(HttpMethod.Post, "/bet/$bet1Id/wager", setup = {
            setBody(Json.encodeToString(Wager(amount = 0, outcome = false, userId = UUID.randomUUID(), betId = bet1Id)))
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
        // good wager
        with(handleRequest(HttpMethod.Post, "/bet/$bet1Id/wager", setup = {
            setBody(Json.encodeToString(Wager(amount = 15, outcome = false, userId = UUID.randomUUID(), betId = bet1Id)))
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertEquals(HttpStatusCode.Created, response.status())
        }
        // multiple wagers on a bet
        with(handleRequest(HttpMethod.Post, "/bet/$bet1Id/wager", setup = {
            setBody(Json.encodeToString(Wager(amount = 1, outcome = false, userId = UUID.randomUUID(), betId = bet1Id)))
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }

    @Test
    fun testReadWagers() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/wager/user", setup = {
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertNotNull(response.content)
            val wagers = Json.decodeFromString<List<Wager>>(response.content ?: "")
            assertEquals(2, wagers.size)
        }
        with(handleRequest(HttpMethod.Get, "/wager/user?complete=true", setup = {
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertNotNull(response.content)
            val wagers = Json.decodeFromString<List<Wager>>(response.content ?: "")
            assertEquals(0, wagers.size)
        }
        with(handleRequest(HttpMethod.Get, "/wager/user?complete=false", setup = {
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertNotNull(response.content)
            val wagers = Json.decodeFromString<List<Wager>>(response.content ?: "")
            assertEquals(2, wagers.size)
        }
    }

    @Test
    fun testOddsCalculation() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/bet", setup = {
            addHeader("Authorization", "Basic $basicAuth2")
        })) {
            assertNotNull(response.content)
            val bets = Json.decodeFromString<List<Bet>>(response.content ?: "")
            val bet2 = bets.filter { it.id!! == bet2Id }[0]
            assertEquals(0, bet2.amount_against)
            assertNull(bet2.odds_against)
            assertNull(bet2.odds_for)
        }
        handleRequest(HttpMethod.Post, "/bet/$bet2Id/wager", setup = {
            setBody(Json.encodeToString(Wager(amount = 5, outcome = false, userId = UUID.randomUUID(), betId = bet2Id)))
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic $basicAuth2")
        })
        with(handleRequest(HttpMethod.Get, "/bet", setup = {
            addHeader("Authorization", "Basic $basicAuth2")
        })) {
            assertNotNull(response.content)
            val bets = Json.decodeFromString<List<Bet>>(response.content ?: "")
            val bet2 = bets.filter { it.id!! == bet2Id }[0]
            assertEquals(5, bet2.amount_against)
            assertEquals(1f, bet2.odds_against)
            assertEquals(2f, bet2.odds_for)
        }
    }

    @Test
    fun testPayout() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Post, "/bet/$bet3Id/complete", setup = {
            setBody("{\"outcome\":true}")
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertEquals(HttpStatusCode.Accepted, response.status())
        }
        with(handleRequest(HttpMethod.Get, "/user/me", setup={addHeader("Authorization", "Basic $basicAuth1")})) {
            val user = Json.decodeFromString<User>(response.content ?: "")
            assertEquals("joel", user.name)
            assertEquals(50, user.amount)
        }
        // make sure the loser didn't get paid out
        with(handleRequest(HttpMethod.Get, "/user/me", setup={addHeader("Authorization", "Basic $basicAuth2")})) {
            val user = Json.decodeFromString<User>(response.content ?: "")
            assertEquals("jack", user.name)
            assertEquals(20, user.amount)
        }
        // should not be able to complete twice
        with(handleRequest(HttpMethod.Post, "/bet/$bet3Id/complete", setup = {
            setBody("{\"outcome\":false}")
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
        // BadRequest when betId not found
        with(handleRequest(HttpMethod.Post, "/bet/${UUID.randomUUID()}/complete", setup = {
            setBody("{\"outcome\":false}")
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Basic $basicAuth1")
        })) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }

    @Test
    fun testSessions() = withTestApplication(Application::main) {
        var authCookie : String
        with(handleRequest(HttpMethod.Post, "/login", setup = {
            setBody("{\"username\":\"joel\", \"password\":\"joel\"}")
            addHeader("Content-Type", "application/json")
        })) {
            assertNotNull(response.cookies[AUTH_COOKIE])
            authCookie = response.cookies[AUTH_COOKIE]!!.value
        }
        with(handleRequest(HttpMethod.Get, "/user/me", setup={addHeader("Cookie", "$AUTH_COOKIE=$authCookie")})) {
            assertEquals(HttpStatusCode.OK, response.status())
            val user = Json.decodeFromString<User>(response.content ?: "")
            assertEquals(user.name, "joel")
        }
    }

    @BeforeTest
    fun before() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Bets)
            SchemaUtils.create(Wagers)
            Wagers.deleteAll()
            Users.deleteAll()
            Bets.deleteAll()

            val user1 = Users.insert {
                it[name] = "joel"
                it[amount] = 20
                it[password_hash] = UserService().hashPassword("joel")
                it[admin] = true
            } get Users.id
            user1Id = user1.value

            val user2 = Users.insert {
                it[name] = "jack"
                it[amount] = 20
                it[password_hash] = UserService().hashPassword("jack")
                it[admin] = false
            } get Users.id
            user2Id = user2.value

            val bet1 = Bets.insert {
                it[description] = "test bet"
                it[complete] = false
            } get Bets.id
            bet1Id = bet1.value

            val bet2 = Bets.insert {
                it[description] = "test bet again"
                it[complete] = false
            } get Bets.id
            bet2Id = bet2.value

            Wagers.insert {
                it[amount] = 10
                it[outcome] = true
                it[user] = user1
                it[bet] = bet2
            }

            val bet3 = Bets.insert {
                it[description] = "test to payout"
                it[complete] = false
            } get Bets.id
            bet3Id = bet3.value

            Wagers.insert {
                it[amount] = 10
                it[outcome] = true
                it[user] = user1
                it[bet] = bet3
            }

            Wagers.insert {
                it[amount] = 20
                it[outcome] = false
                it[user] = user2
                it[bet] = bet3
            }

            val bet4 = Bets.insert {
                it[description] = "complete bet"
                it[complete] = true
            } get Bets.id
            bet4Id = bet4.value
        }
    }
}
