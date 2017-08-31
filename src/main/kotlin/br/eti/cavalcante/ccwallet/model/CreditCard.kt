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

    fun pay(ammount: BigDecimal): Triple<BigDecimal, BigDecimal, CreditCard> {
        val freeAmount = userLimit - usage

        val paidValue = if(freeAmount < ammount) freeAmount else ammount
        usage += paidValue

        return Triple(ammount - paidValue, paidValue, this)
    }

}
