package br.eti.cavalcante.ccwallet.model

import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.Entity

@Entity
class CreditCard(
    val name : String,
    val number : String,
    val expirationDate : LocalDate,
    val securityNumber : Int,
    val dueDate : LocalDate,
    val userLimit : BigDecimal,
    val maxLimit : BigDecimal,
    var usage : BigDecimal
): BaseModel() {

    fun pay(amount: BigDecimal): Triple<BigDecimal, BigDecimal, CreditCard> {
        val freeAmount = userLimit - usage

        return if(freeAmount >= amount) {
            val paidValue = amount
            usage += paidValue
            Triple(amount - paidValue, paidValue, this)
        } else {
            Triple(amount, BigDecimal.ZERO, this)
        }
    }

}
