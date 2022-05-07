// @file:Suppress("EXPERIMENTAL_API_USAGE")
// package trashbet

// import io.ktor.application.*
// import io.ktor.auth.*
// import io.ktor.routing.*
// import io.ktor.sessions.*
// import io.ktor.util.*
// import io.ktor.util.pipeline.*
// import kotlinx.serialization.Serializable
// import java.util.*

// data class UserPrincipal(
//         @Serializable(with = UUIDSerializer::class) val id: UUID,
//         val name: String,
//         val admin: Boolean,
// ) : Principal

// const val AUTH_COOKIE = "trashbet_session"

// fun Authentication.Configuration.registerAuth() {
//     basic("basic") {
//         skipWhen { call -> call.sessions.get<UserPrincipal>() != null }
//         validate { credentials ->
//             val user = UserService().loginUser(credentials.name, credentials.password)
//             if (user != null) {
//                 UserPrincipal(user.id!!, user.name, user.admin)
//             } else {
//                 null
//             }
//         }
//     }
// }

// @Suppress("UNUSED_PARAMETER")
// class AuthN(config: Configuration) {

//     fun interceptPipeline(pipeline: ApplicationCallPipeline) {
//         pipeline.insertPhaseAfter(ApplicationCallPipeline.Features, Authentication.ChallengePhase)
//         pipeline.insertPhaseAfter(Authentication.ChallengePhase, AuthorizationPhase)

//         pipeline.intercept(AuthorizationPhase) {
//             val principal =
//                     call.getUserPrincipal() ?: throw AuthorizationException("Missing principal")
//             // Apply auth
//             if (!principal.admin) throw AuthorizationException("Admin required")
//         }
//     }

//     class Configuration

//     companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, AuthN> {
//         override val key = AttributeKey<AuthN>("AuthN")

//         val AuthorizationPhase = PipelinePhase("Authorization")

//         override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): AuthN {
//             val configuration = Configuration().apply(configure)
//             return AuthN(configuration)
//         }
//     }
// }

// class AuthorizedRouteSelector(private val description: String) :
//         RouteSelector(RouteSelectorEvaluation.qualityConstant) {
//     override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant

//     override fun toString(): String = "(authorize ${description})"
// }

// fun Route.adminRequired(build: Route.() -> Unit): Route {
//     val authorizedRoute = createChild(AuthorizedRouteSelector("admin"))
//     application.feature(AuthN).interceptPipeline(authorizedRoute)
//     authorizedRoute.build()
//     return authorizedRoute
// }

// fun ApplicationCall.getUserPrincipal(): UserPrincipal? {
//     return this.sessions.get<UserPrincipal>() ?: this.authentication.principal()
// }
