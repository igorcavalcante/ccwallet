package br.eti.cavalcante.ccwallet.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.http.HttpStatusCode.Companion.OK

@JsonIgnoreProperties(value = *arrayOf("success", "code"))
open class OperationResult(val content: Any, val success: Boolean = true, val code: HttpStatusCode = OK)