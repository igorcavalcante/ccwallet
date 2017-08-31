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

    fun purchase(amount: BigDecimal): List<CreditCard> {
        val bestCards = getBestCards(amount)
        return pay(amount, bestCards, listOf())
    }

    private fun pay(amount: BigDecimal, cards: List<CreditCard>, paidCards: List<CreditCard>): List<CreditCard> {
        return if(amount == ZERO) {
            paidCards
        } else {
            pay(cards.first().pay(amount), cards.drop(0), paidCards + cards.first())
        }
    }

    private fun getBestCards(amount: BigDecimal) : List<CreditCard>{
        return listOf(cards.sortedBy { it.dueDate }.last())
    }

}

