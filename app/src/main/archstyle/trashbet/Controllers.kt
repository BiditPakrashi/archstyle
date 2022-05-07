 package trashbet

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import java.io.File
import java.util.*

// fun Route.userController(userService: UserService) {
//     route("/user") {
//         get("/me") {
//             val principal = call.getUserPrincipal()!!
//             val user = userService.getUserByName(principal.name)
//             user?.let { call.respond(HttpStatusCode.OK, user) }
//         }

//         adminRequired {
//             get("/") {
//                 val users = userService.getAllUsers()
//                 call.respond(users)
//             }

//             post("/promote") {
//                 val userId = call.request.queryParameters["userId"]!!
//                 val userUUID = UUID.fromString(userId)
//                 userService.promoteUser(userUUID)
//                 call.respond(HttpStatusCode.Accepted)
//             }
//         }
//     }
// }

// fun Route.betController(betService: BetService, wagerService: WagerService) {
//     route("/bet") {
//         get("/") {
//             val filter_complete = call.request.queryParameters["complete"]?.toBoolean()
//             val bets = betService.getAllBets(filter_complete)
//             call.respond(bets)
//         }
//         adminRequired {
//             post("/") {
//                 var bet = call.receive<Bet>()
//                 bet = betService.addBet(bet)
//                 call.respond(HttpStatusCode.Created, bet)
//             }

//             post("/{betId}/complete") {
//                 val betId = call.parameters["betId"]!!
//                 val betUUID = UUID.fromString(betId)
//                 val betCompletion = call.receive<BetCompletion>()
//                 val bet = betService.completeBet(betUUID, betCompletion.outcome)
//                 wagerService.payoutWagers(bet)
//                 call.respond(HttpStatusCode.Accepted)
//             }
//         }
//     }

//     route("/bet/{betId}/wager") {
//         get("/") {
//             val betId = call.parameters["betId"]!!
//             val betUUID = UUID.fromString(betId)
//             val wagers = wagerService.getWagersByBetId(betUUID)
//             call.respond(wagers)
//         }

//         post("/") {
//             val betId = call.parameters["betId"]!!
//             val betUUID = UUID.fromString(betId)
//             var wager = call.receive<Wager>()
//             if (betUUID != wager.betId) {
//                 call.respond(HttpStatusCode.BadRequest, "bet ids don't match")
//                 return@post
//             }
//             if (wager.amount <= 0) {
//                 call.respond(HttpStatusCode.BadRequest, "bad wager amount")
//                 return@post
//             }
//             val userId = call.getUserPrincipal()?.id!!
//             val currentWagers = wagerService.getWagersByUserId(userId, null)
//             if (currentWagers.any { it.betId == wager.betId }) {
//                 call.respond(HttpStatusCode.BadRequest, "user already has wager for bet")
//                 return@post
//             }
//             wager = wagerService.addWagerForUser(wager, userId)
//             call.respond(HttpStatusCode.Created, wager)
//         }
//     }
// }

// fun Route.wagerController(wagerService: WagerService) {
//     route("/wager/user") {
//         get("/") {
//             val filter_complete = call.request.queryParameters["complete"]?.toBoolean()
//             val principal = call.getUserPrincipal()!!
//             val wagers = wagerService.getWagersByUserId(principal.id, filter_complete)
//             call.respond(wagers)
//         }
//     }
// }

// fun Route.unauthedControllers(userService: UserService) {
//     route("/") {
//         get("/health") {
//             call.respondText("healthy")
//         }

//         post("/register") {
//             val registration = call.receive<UserRegistration>()
//             var user = User(name=registration.username, amount=0)
//             user = userService.addUser(user, registration.password)
//             call.respond(user)
//         }

//         post("/login") {
//             val creds = call.receive<UserRegistration>()
//             val user = userService.loginUser(creds.username, creds.password)
//             if (user == null) {
//                 call.respond(HttpStatusCode.Unauthorized)
//                 return@post
//             }
//             call.sessions.set(UserPrincipal(user.id!!, user.name, admin = true))
//             call.respond(HttpStatusCode.Accepted)
//         }
//     }
// }

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
