package trashbet
import java.util.*
import kotlin.math.roundToInt

// class UserService {
//     fun getAllUsers(): List<User> = transaction {
//         Users.selectAll().map{ toUser(it) }
//     }

//     fun getUserByName(name: String): User? = transaction {
//         Users.select {
//             (Users.name eq name)
//         }.mapNotNull {
//             toUser(it)
//         }.singleOrNull()
//     }

//     fun addUser(user: User, password: String): User {
//         if (getUserByName(user.name) != null) {
//             throw Exception("username already exists")
//         }
//         val is_first = transaction { Users.selectAll().count() == 0.toLong() }
//         val name = transaction {
//             Users.insert {
//                 it[name] = user.name
//                 it[amount] = 20
//                 it[password_hash] = hashPassword(password)
//                 it[admin] = is_first
//             }
//         } get Users.name
//         return getUserByName(name)!!
//     }

//     fun loginUser(username: String, password: String): User? {
//         val user = getUserByName(username) ?: return null
//         val passwordHash = transaction {
//             Users.select {
//                 (Users.id eq user.id)
//             }.withDistinct().map {
//                 it[Users.password_hash]
//             }.single()
//         }
//         if (checkPassword(password, passwordHash)) {
//             return user
//         }
//         return null
//     }

//     fun hashPassword(password: String): String {
//         return BCrypt.hashpw(password, BCrypt.gensalt())
//     }

//     private fun checkPassword(plaintext: String, passwordHash: String): Boolean {
//         return BCrypt.checkpw(plaintext, passwordHash)
//     }

//     fun promoteUser(userId: UUID) {
//         val rows = transaction {
//             Users.update({ Users.id eq userId }) {
//                 it[admin] = true
//             }
//         }
//         if (rows != 1) {
//             throw InputException("This user is already admin or didn't exist")
//         }
//     }

//     private fun toUser(row: ResultRow): User = User(
//             id = row[Users.id].value,
//             name = row[Users.name],
//             amount = row[Users.amount],
//             admin = row[Users.admin]
//     )
// }

// class BetService {
//     fun getAllBets(complete: Boolean?): List<Bet> = transaction {
//         val query = complete?.let {
//             Bets.select {
//                 (Bets.complete eq complete)
//             }
//         } ?: run {
//             Bets.selectAll()
//         }
//         query.map{ toBet(it) }
//     }

//     private fun getBetById(id: UUID): Bet? = transaction {
//         Bets.select {
//             (Bets.id eq id)
//         }.mapNotNull {
//             toBet(it)
//         }.singleOrNull()
//     }

//     fun addBet(bet: Bet): Bet = transaction {
//         val id = Bets.insert {
//             it[description] = bet.description
//             it[complete] = false
//         } get Bets.id
//         getBetById(id.value)!!
//     }

//     fun completeBet(betId: UUID, new_outcome: Boolean): Bet = transaction {
//         val rows = Bets.update({(Bets.id eq betId) and (Bets.complete eq false)}) {
//             it[complete] = true
//             it[outcome] = new_outcome
//         }
//         if (rows != 1) {
//             throw InputException("This bet did not exist or is complete")
//         }
//         getBetById(betId)!!
//     }

//     private fun toBet(row: ResultRow): Bet {
//         val wagers = transaction {
//             Wagers.select {
//                 (Wagers.bet eq row[Bets.id])
//             }
//         }
//         val amountfor = wagers.filter { it[Wagers.outcome] }.fold(0){sum, it -> sum + it[Wagers.amount]}
//         val amountagainst = wagers.filterNot { it[Wagers.outcome] }.fold(0){sum, it -> sum + it[Wagers.amount]}
//         // odds calculation
//         var oddsfor: Float? = null
//         var oddsagainst: Float? = null
//         if (amountfor != 0 && amountagainst != 0) {
//             oddsfor = if (amountfor > amountagainst) (amountfor.toFloat() / amountagainst.toFloat()) else 1f
//             oddsagainst = if (amountagainst > amountfor) (amountagainst.toFloat() / amountfor.toFloat()) else 1f
//         }
//         return Bet(
//                 id = row[Bets.id].value,
//                 description = row[Bets.description],
//                 complete = row[Bets.complete],
//                 outcome = row[Bets.outcome],
//                 amount_for = amountfor,
//                 amount_against = amountagainst,
//                 odds_for = oddsfor,
//                 odds_against = oddsagainst,
//         )
//     }
// }

// class WagerService {
//     fun getWagersByBetId(betId: UUID): List<Wager> = transaction {
//         Wagers.select {
//             (Wagers.bet eq betId)
//         }.mapNotNull {
//             toWager(it)
//         }
//     }

//     fun getWagersByUserId(userId: UUID, complete: Boolean?): List<Wager> = transaction {
//         var query = Wagers.innerJoin(Bets).select {
//             (Wagers.user eq userId)
//         }
//         complete?.let {
//             query = query.andWhere { (Bets.complete eq complete) }
//         }
//         query.mapNotNull {
//             toWager(it)
//         }
//     }

//     private fun getWagerById(id: UUID): Wager? = transaction {
//         Wagers.select {
//             (Wagers.id eq id)
//         }.mapNotNull {
//             toWager(it)
//         }.singleOrNull()
//     }

//     fun addWagerForUser(wager: Wager, userId: UUID): Wager {
//         val amount = transaction {
//             Users.select {
//                 (Users.id eq userId)
//             }.single()[Users.amount]
//         }
//         if (wager.amount > amount) {
//             throw InputException("User didn't have enough to place that bet")
//         }
//         val betId = transaction {
//             Bets.select {
//                 (Bets.id eq wager.betId) and
//                 (Bets.complete eq false)
//             }.single()[Bets.id]
//         }
//         val wagerId = transaction {
//             Users.update({Users.id eq userId}) {
//                 it[Users.amount] = amount - wager.amount
//             }
//             Wagers.insert {
//                 it[Wagers.amount] = wager.amount
//                 it[outcome] = wager.outcome
//                 it[user] = EntityID<UUID>(userId, Users)
//                 it[bet] = betId
//             } get Wagers.id
//         }
//         return getWagerById(wagerId.value)!!
//     }

//     fun payoutWagers(bet: Bet) = transaction {
//         val total = bet.amount_against + bet.amount_for
//         val outcome_amount = if (bet.outcome!!) bet.amount_for else bet.amount_against
//         val wagers = getWagersByBetId(bet.id!!)
//         val winners = wagers.filter { it.outcome == bet.outcome }
//         for (w in winners) {
//             val percent = w.amount.toFloat() / outcome_amount.toFloat()
//             val earnings = total * percent
//             Users.update({Users.id eq w.userId}) {
//                 with(SqlExpressionBuilder) {
//                     it.update(amount, amount.plus(earnings.roundToInt()))
//                 }
//             }
//         }
//     }

//     private fun toWager(row: ResultRow): Wager = Wager(
//             id = row[Wagers.id].value,
//             amount = row[Wagers.amount],
//             outcome = row[Wagers.outcome],
//             userId = row[Wagers.user].value,
//             betId = row[Wagers.bet].value,
//     )
// }
