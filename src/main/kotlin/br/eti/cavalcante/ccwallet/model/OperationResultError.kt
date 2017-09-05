package br.eti.cavalcante.ccwallet.model

import br.eti.cavalcante.ccwallet.model.OperationResult.ResultCode.*


class OperationResultError(
    message: String, code: ResultCode = ERROR
) : OperationResult(false, message, code)
