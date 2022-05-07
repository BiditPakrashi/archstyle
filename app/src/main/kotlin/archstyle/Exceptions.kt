package trashbet

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

open class AppException(message: String): Exception(message)

class InputException(message: String): AppException(message)

class AuthorizationException(message: String): AppException(message)

fun StatusPages.Configuration.registerExceptionHandling() {
    exception<Exception> {
        call.respond(HttpStatusCode.InternalServerError)
    }

    exception<AppException> {
        call.respond(HttpStatusCode.InternalServerError)
    }

    exception<InputException> { cause ->
        call.respond(HttpStatusCode.BadRequest, cause.toString())
    }

    exception<AuthorizationException> { cause ->
        call.respond(HttpStatusCode.Unauthorized, cause.toString())
    }
}