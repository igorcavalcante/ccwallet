package br.eti.cavalcante.ccwallet.model

import br.eti.cavalcante.ccwallet.exceptions.ValidationException
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
class Wallet(
    @OneToOne(cascade = arrayOf(CascadeType.ALL))
    val user: User,
    @OneToMany(cascade = arrayOf(CascadeType.ALL))
    val cards: List<CreditCard>,
    val userLimit: BigDecimal
): BaseModel() {

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

    //testar com free limit
    fun purchase(amount: BigDecimal): PurchaseResult {
        val cardsOrdered = sortedCards()
        val cardUsed = cardsOrdered.find { it.pay(amount) }

        val result = if(cardUsed != null) {
            PurchaseResult(amount, listOf(CreditCard.CreditCardResult(amount, cardUsed)))
        } else {
            payPartialy(amount, cardsOrdered, PurchaseResult(amount))
        }
        save()
        return result
    }

    private tailrec fun payPartialy(amount: BigDecimal, cards: List<CreditCard>, result: PurchaseResult): PurchaseResult {
        return when (amount) {
            ZERO -> result
            else -> {
                val individualResult = cards.first().payPartial(amount)
                val incrementedResult = if(individualResult.second.amountPaid > ZERO) result.addPayment(individualResult.second) else result
                payPartialy(individualResult.first, cards.drop(1), incrementedResult)
            }
        }
    }

    fun sortedCards() : List<CreditCard>{
        return cards.sortedWith(Comparator { o1, o2 ->
            when {
                o1.dueDate > o2.dueDate -> -1
                o1.dueDate < o2.dueDate -> 1
                else -> o1.freeAmount().compareTo(o2.freeAmount())
            }
        })
    }

}

