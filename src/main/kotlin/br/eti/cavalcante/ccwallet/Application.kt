package br.eti.cavalcante.ccwallet

import br.eti.cavalcante.ccwallet.model.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ebean.DuplicateKeyException
import io.ebean.Ebean
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.ApplicationCall
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.auth.UserIdPrincipal
import org.jetbrains.ktor.auth.authentication
import org.jetbrains.ktor.auth.basicAuthentication
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.features.Compression
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.http.HttpStatusCode.Companion.BadRequest
import org.jetbrains.ktor.request.receiveText
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.*
import java.math.BigDecimal
import java.security.MessageDigest
import java.text.SimpleDateFormat

val contentType = ContentType.parse("application/json")

class LimitRequest(val limit: BigDecimal)
class PurchaseRequest(val amount: BigDecimal)

suspend fun genResp(resp: OperationResult, call: ApplicationCall) {
    call.respondText(json.writer.writeValueAsString(resp), contentType, resp.code)
}

object json {
    val mapper = ObjectMapper().registerModule(KotlinModule()).registerModule(JavaTimeModule())
    init {
        mapper.dateFormat = SimpleDateFormat("yyyy-MM-dd")
    }

    val writer = mapper.writerWithDefaultPrettyPrinter()
}

fun Application.main() {

    install(DefaultHeaders)
    install(Compression)
    install(CallLogging)
    routing {
        post("/wallet") {
            val body = call.receiveText()
            val wallet: Wallet = json.mapper.readValue(body, Wallet::class.java)
            try {
                wallet.save()
                genResp(OperationResult("Carteira criada com sucesso"), call)
            }catch (e: DuplicateKeyException) {
                genResp(OperationResultError("Usuário já existe", BadRequest), call)
            }catch (e: Exception) {
                genResp(OperationResultError(e.message!!, BadRequest), call)
            }
        }

        routing {
            var user : User? = null
            authentication {
                basicAuthentication("ccwallet") {
                    user = User.auth(it.name, digest(it.password))
                    if (user != null) UserIdPrincipal(it.name) else null
                }
            }

            post("/wallet/card") {
                val body = call.receiveText()
                val card: CreditCard = json.mapper.readValue(body, CreditCard::class.java)
                genResp(user!!.wallet.addCard(card), call)
            }

            post("/wallet/card/{number}/pay") {
                val number = call.parameters["number"] as String
                val resp = user!!.wallet.payInvoice(number)
                genResp(resp, call)
            }

            delete("/wallet/card/{number}") {
                val number = call.parameters["number"] as String
                val resp = user!!.wallet.removeCard(number)
                genResp(resp, call)
            }

            get("/wallet") {
                val wallets = Ebean.find(Wallet::class.java).where().findList()
                genResp(OperationResult(wallets), call)
            }

            put("/wallet/limit") {
                val body = call.receiveText()
                val request: LimitRequest = json.mapper.readValue(body, LimitRequest::class.java)
                genResp(user!!.wallet.updateLimit(request.limit), call)
            }

            post("/wallet/purchase") {
                val body = call.receiveText()
                val request: PurchaseRequest = json.mapper.readValue(body, PurchaseRequest::class.java)
                genResp(user!!.wallet.purchase(request.amount), call)
            }
        }
    }
}
