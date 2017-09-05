package br.eti.cavalcante.ccwallet.model

import br.eti.cavalcante.ccwallet.model.OperationResult.ResultCode.*


open class OperationResult(val success: Boolean = true, val message: String = "", val code: ResultCode = OK) {

    enum class ResultCode(val httpCode: Int) {
        NOT_FOUND(404),
        ERROR(500),
        OK(200),
        CREATED(201)
    }
}