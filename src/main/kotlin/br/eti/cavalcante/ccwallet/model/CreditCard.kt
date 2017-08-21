package br.eti.cavalcante.ccwallet.model

import java.math.BigDecimal
import java.time.LocalDate

class CreditCard(
    val name: String, val number: String, val expires: LocalDate, val securityNumber: Int,
    val userLimit: BigDecimal, val maxLimit: BigDecimal, val usage: BigDecimal
) {

}