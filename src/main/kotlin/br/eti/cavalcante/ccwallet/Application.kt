package br.eti.cavalcante.ccwallet

import br.eti.cavalcante.ccwallet.model.OperationResult
import br.eti.cavalcante.ccwallet.model.OperationResultError
import br.eti.cavalcante.ccwallet.model.Wallet
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ebean.Ebean
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.ApplicationCall
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.features.Compression
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.http.HttpStatusCode.Companion.BadRequest
import org.jetbrains.ktor.request.receiveText
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.post
import org.jetbrains.ktor.routing.routing
import java.text.SimpleDateFormat

val contentType = ContentType.parse("application/json")

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
        get("/wallet") {
            val wallets = Ebean.find(Wallet::class.java).where().findList()
            genResp(OperationResult(wallets), call)
        }

        post("/wallet") {
            val body = call.receiveText()
            val wallet: Wallet = json.mapper.readValue(body, Wallet::class.java)
            try {
                wallet.save()
                genResp(OperationResult("Carteira criada com sucesso"), call)
            }catch (e: Exception) {
                genResp(OperationResultError(e.message!!, BadRequest), call)
            }
        }
    }
}
