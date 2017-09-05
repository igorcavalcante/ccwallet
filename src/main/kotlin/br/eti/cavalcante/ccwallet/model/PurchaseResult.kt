package br.eti.cavalcante.ccwallet.model

import java.math.BigDecimal


class PurchaseResult(
    val amount : BigDecimal,
    val entries: List<CreditCard.CreditCardResult> = listOf(),
    success: Boolean = true,
    message: String = ""
): OperationResult(success, message) {

    fun addPayment(payment: CreditCard.CreditCardResult) =
        PurchaseResult(amount, entries + payment)

}
