 package archstyle
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import java.io.File
import java.util.*



fun Route.unauthgithubController(unAuthGithubService: UnAuthGithubService) {
    route("/unauth/population") {
        get("/") {
            println("Request Recived")
            val population = unAuthGithubService.getgithubInfo()
            println("population" + population)
            call.respond(population)
        }
    }
}

fun Route.unauthedControllers() {
    route("/") {
        get("/health") {
            call.respondText(" Server healthy")
        }

    }
}



fun Route.webController() {
    static("/") {
        staticRootFolder = File("web/public")
        file("build/bundle.js")
        file("build/bundle.css")
        file("build/bundle.js.map")
        file("global.css")
        file("index.html")
        file("favicon.png")
        default("index.html")
    }
}
