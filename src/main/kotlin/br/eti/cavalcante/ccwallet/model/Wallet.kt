package br.eti.cavalcante.ccwallet.model

import br.eti.cavalcante.ccwallet.exceptions.ValidationException
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.smartcardio.Card

@Entity
class Wallet(
    @OneToOne(cascade = arrayOf(CascadeType.ALL))
    val user: User,
    @OneToMany(cascade = arrayOf(CascadeType.ALL))
    val cards: List<CreditCard>,
    val userLimit: BigDecimal
): BaseModel() {

/*    fun save(): Wallet {
        if(userLimit > calculateMaxLimit()) {
            throw ValidationException(
                "O Limite da carteira (R$ ${userLimit}) não pode ser maior que a soma do limite dos cartões (R$${calculateMaxLimit()})."
            )
        }

        val self = this
        WalletData.new {
            user = UserData.new {
                name = self.user.name
                userName = self.user.userName
                password = self.user.password
            }
            realLimit = ZERO
        }

        return this
    }*/

    fun calculateMaxLimit() = cards.fold(ZERO) { acc, card -> acc + card.maxLimit }

    //Verificar duplicidade dos cartoes
    override fun save() {
        if(userLimit > calculateMaxLimit()) {
            throw ValidationException(
                    "O Limite da carteira (R$ ${userLimit}) não pode ser maior que a soma do limite dos cartões (R$${calculateMaxLimit()})."
            )
        }

        super.save()
    }

    fun purchase(amount: BigDecimal): PurchaseResult {
        val orderedByDueDate = cards.sortedByDescending { it.dueDate }
        val result = pay(amount, orderedByDueDate, PurchaseResult(amount))
        save()
        return result
    }

    private tailrec fun pay(amount: BigDecimal, cards: List<CreditCard>, result: PurchaseResult): PurchaseResult {
        return when {
            amount == ZERO -> result
//            cards.isEmpty() -> pay(amount, cards.sortedByDescending { it.dueDate }, result)
            else -> {
                val individualResult = cards.first().pay(amount)
                pay(individualResult.first, cards.drop(1), result.addPayment(individualResult))
            }
        }
    }

    private fun getBestCards(amount: BigDecimal) : List<CreditCard>{
        return cards.sortedBy { it.dueDate }.asReversed()
    }

}

