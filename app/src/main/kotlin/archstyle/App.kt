package archstyle
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
import archstyle.webController
import archstyle.unauthgithubController
import io.ktor.gson.gson


fun Application.main() {
    @Suppress("EXPERIMENTAL_API_USAGE")


    install(StatusPages) {
        registerExceptionHandling()
    }

    install(ContentNegotiation) {
    gson {
        setPrettyPrinting()
    }
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

   val unAuthGithubService = UnAuthGithubService()
    install(Routing) {
        unauthedControllers()
      webController()
      unauthgithubController(unAuthGithubService)

    }
}

@Suppress("UNUSED_PARAMETER")
fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait=true)
}
