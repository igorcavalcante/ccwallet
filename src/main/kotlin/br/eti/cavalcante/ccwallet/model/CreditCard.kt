package br.eti.cavalcante.ccwallet.model

import br.eti.cavalcante.ccwallet.CryptDelegator
import br.eti.cavalcante.ccwallet.CryptUtil
import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Transient

@Entity
class CreditCard(
        @Transient var name: String,
        @Transient var number: String,
        val expirationDate : LocalDate,
        @Transient var securityNumber : Int,
        var dueDate : LocalDate,
        val cardLimit : BigDecimal,
        var usage : BigDecimal
): BaseModel() {

    init {
        encFields()
        decFields()
    }

    @JsonIgnore var cryptName: String? = null
    @JsonIgnore var cryptNumber: String? = null
    @JsonIgnore var cryptSecurityNumber: String? = null

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

    override fun save() {
/*        if(key == null)
            throw ValidationException("A chave precisa ser definida antes de salvar o cartão de crédito")
        else {*/
            super.save()
        //}
    }

    fun encFields() {
        if(cryptName == null || cryptNumber == null || cryptSecurityNumber == null) {
            cryptName = CryptUtil.enc(name)
            cryptNumber = CryptUtil.enc(number)
            cryptSecurityNumber = CryptUtil.enc(securityNumber.toString())
        }
    }

    fun decFields() {
        if(name == null || number == null || securityNumber == null) {
            name = CryptUtil.dec(cryptName!!)
            number = CryptUtil.dec(cryptNumber!!)
            securityNumber = CryptUtil.dec(cryptSecurityNumber!!).toInt()
        }
    }

}
