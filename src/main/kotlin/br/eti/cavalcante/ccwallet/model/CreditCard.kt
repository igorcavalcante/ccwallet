package br.eti.cavalcante.ccwallet.model

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDate
import java.time.Month
import javax.persistence.Entity

@Entity
class CreditCard(
        val name : String,
        val number : String,
        val expirationDate : LocalDate,
        val securityNumber : Int,
        var dueDate : LocalDate,
        val cardLimit : BigDecimal,
        var usage : BigDecimal
): BaseModel() {

    class CreditCardResult(val amountPaid: BigDecimal, val card: CreditCard)

    fun pay(amount: BigDecimal): Boolean {
        return if(freeAmount() >= amount) {
            usage += amount
            true
        } else {
            false
        }
    }

    fun payPartial(amount: BigDecimal): Pair<BigDecimal, CreditCardResult> {
        return if(freeAmount() >= amount) {
            usage += amount
            Pair(ZERO, CreditCardResult(amount, this))
        } else {
            val paidValue = freeAmount()
            usage += paidValue
            Pair(amount - paidValue, CreditCardResult(paidValue, this))
        }
    }

    fun freeAmount() = cardLimit - usage

    fun payInvoice() {
        this.dueDate = this.dueDate.plusMonths(1)
    }

}
