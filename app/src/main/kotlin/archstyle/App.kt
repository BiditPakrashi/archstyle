package trashbet

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.Netty
import io.ktor.sessions.*
import kotlinx.serialization.json.Json
import trashbet.webController


fun Application.main() {
    @Suppress("EXPERIMENTAL_API_USAGE")
    // val deploymentEnvironment = environment.config.property("ktor.deployment.environment").getString()
    // val dbUser = environment.config.propertyOrNull("ktor.database.user")
    // val dbPass = environment.config.propertyOrNull("ktor.database.pass")
    // val dbDb = environment.config.propertyOrNull("ktor.database.db")
    // var authenticationScheme = "basic"

    // when (deploymentEnvironment) {
    //     "test" -> {
    //         Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    //         authenticationScheme = "basic"
    //     }
    //     "development" -> {
    //         Database.connect("jdbc:postgresql://localhost:5432/${dbDb!!.getString()}", driver = "org.postgresql.Driver",
    //                 user = dbUser!!.getString(), password = dbPass!!.getString())
    //         seedData(true)
    //     }
    //     "production" -> {
    //         Database.connect("jdbc:postgresql://db:5432/${dbDb!!.getString()}", driver = "org.postgresql.Driver",
    //                 user = dbUser!!.getString(), password = dbPass!!.getString())
    //         seedData(false)
    //     }
    // }

    // install(Authentication) {
    //     registerAuth()
    // }

    // install(AuthN)

    // install(Sessions) {
    //     cookie<UserPrincipal>(AUTH_COOKIE, storage = SessionStorageMemory()) {}
    // }

    install(StatusPages) {
        registerExceptionHandling()
    }

    install(ContentNegotiation) {
        register(ContentType.Application.Json, SerializationConverter(Json { prettyPrint = true }))
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        header(HttpHeaders.ContentType)
        header(HttpHeaders.Cookie)
        exposeHeader(HttpHeaders.SetCookie)
        exposeHeader(HttpHeaders.ContentLength)
        allowCredentials = true
        maxAgeInSeconds = 360
        anyHost() //LOL
    }

  //  val userService = UserService()
    // val betService = BetService()
    // val wagerService = WagerService()

    install(Routing) {
      //  unauthedControllers(userService)
      webController()
        // authenticate(authenticationScheme) {
        //     userController(userService)
        //     betController(betService, wagerService)
        //     wagerController(wagerService)
        // }
    }
}

@Suppress("UNUSED_PARAMETER")
fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait=true)
}
