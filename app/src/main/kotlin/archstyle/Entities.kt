@file:Suppress("EXPERIMENTAL_API_USAGE", "unused")

package trashbet

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.descriptors.SerialDescriptor
import java.util.UUID

@Serializable
data class User(
        @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
        val name: String,
        val amount: Int,
        val admin: Boolean = false,
)

@Serializable
data class UserRegistration(
        val username: String,
        val password: String,
)

// object Users: UUIDTable() {
//     val name = varchar("name", 255)
//     val amount = integer("amount")
//     val password_hash = varchar("password", 255)
//     val admin = bool("admin")
// }

@Serializable
data class Bet(
        @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
        val description: String,
        val complete: Boolean,
        val outcome: Boolean? = null,
        val amount_for: Int = 0,
        val amount_against: Int = 0,
        val odds_for: Float? = null,
        val odds_against: Float? = null,
)

// object Bets: UUIDTable() {
//     val description = text("description")
//     val complete = bool("complete")
//     val outcome = bool("outcome").nullable()
// }

@Serializable
data class BetCompletion(
        val outcome: Boolean
)

@Serializable
data class Wager(
        @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
        val amount: Int,
        val outcome: Boolean,
        @Serializable(with = UUIDSerializer::class) val userId: UUID,
        @Serializable(with = UUIDSerializer::class) val betId: UUID,
)

// object Wagers: UUIDTable() {
//     val amount = integer("amount")
//     val outcome = bool("outcome")

//     val user = reference("user", Users)
//     val bet = reference("bet", Bets)
// }

@ExperimentalSerializationApi
@Serializer(forClass = UUID::class)
    object UUIDSerializer : KSerializer<UUID> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: UUID) {
            encoder.encodeString(value.toString())
        }

        override fun deserialize(decoder: Decoder): UUID {
            return UUID.fromString(decoder.decodeString())
        }
    }
