package br.eti.cavalcante.ccwallet.model

import org.jetbrains.ktor.http.HttpStatusCode

class OperationResultError(
    content: Any, code: HttpStatusCode = HttpStatusCode.InternalServerError
) : OperationResult(content, false, code)
