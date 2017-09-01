package br.eti.cavalcante.ccwallet.model

import java.math.BigDecimal


class PurchaseResult(val amount : BigDecimal, val entries: List<CreditCard.CreditCardResult> = listOf()) {

    fun addPayment(payment: CreditCard.CreditCardResult) =
        PurchaseResult(amount, entries + payment)

}
