package br.eti.cavalcante.ccwallet.model

import java.math.BigDecimal


class PurchaseResult(val amount : BigDecimal, val entries: List<Pair<BigDecimal, CreditCard>> = listOf()) {

    fun addPayment(payment: Triple<BigDecimal, BigDecimal, CreditCard>) =
        PurchaseResult(amount, entries + Pair(payment.second, payment.third))

}
