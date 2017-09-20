package br.eti.cavalcante.ccwallet.model

import br.eti.cavalcante.ccwallet.digest
import br.eti.cavalcante.ccwallet.exceptions.ValidationException
import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.ktor.http.HttpStatusCode
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
class Wallet(
    @OneToOne(mappedBy = "wallet", cascade = arrayOf(CascadeType.ALL))
    val user: User,
    @OneToMany(cascade = arrayOf(CascadeType.ALL))
    var cards: List<CreditCard>,
    limit: BigDecimal
): BaseModel() {

    @JsonProperty("limit")
    var userLimit = limit
    private set

    private fun calculateMaxLimit() = cards.fold(ZERO) { acc, card -> acc + card.cardLimit }

    override fun save() {
        if(userLimit > calculateMaxLimit()) {
            throw ValidationException(
                "O Limite da carteira (R$ ${userLimit}) não pode ser maior que a soma do limite dos cartões (R$${calculateMaxLimit()})."
            )
        }

        user.password = digest(user.password)
        super.save()
    }

    fun purchase(amount: BigDecimal): PurchaseResult {
        return if(amount > getFreeAmount()) {
            PurchaseResult(
                ZERO,
                listOf(),
                false,
                "Não foi possível realizar a transação no valor de $amount, pois o saldo disponível na carteira é de: ${getFreeAmount()}"
            )
        } else {
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

    private fun getFreeAmount() = minOf(userLimit, cards.fold(ZERO) { acc, card  -> acc + card.freeAmount()})

    fun updateLimit(amount: BigDecimal): OperationResult {
        return if(amount > calculateMaxLimit()) {
            OperationResultError("O valor $amount ultrapassa o somatório dos limites dos cartões na carteira, que é ${calculateMaxLimit()}", HttpStatusCode.BadRequest)
        } else {
            userLimit = amount
            save()
            OperationResult("Limite atualizado para $amount")
        }
    }

    fun removeCard(cardNumber: String): OperationResult {
        val card = cards.find { it.number == cardNumber }
        return if(card != null) {
            cards -= card
            if(calculateMaxLimit() < userLimit) { userLimit = calculateMaxLimit() }
            card.delete()
            save()
            OperationResult("Cartão com o número $cardNumber foi removido com sucesso da carteira")
        } else {
            OperationResultError("Cartão com o número $cardNumber não foi encontrado", HttpStatusCode.NotFound)
        }
    }

    fun addCard(card: CreditCard): OperationResult {
        val exists = cards.any { it.number == card.number }
        return if(exists) {
            OperationResultError("Cartão com o número ${card.number} já existe na carteira", HttpStatusCode.BadRequest)
        } else {
            OperationResult("Cartão cadastrado com sucesso!")
        }
    }

    fun payInvoice(cardNumber: String): OperationResult {
        val card = cards.find { it.number == cardNumber }
        return if(card != null) {
            card.payInvoice()
            card.update()
            OperationResult("Fatura do cartão: $cardNumber paga. O novo vencimento é: ${card.dueDate}")
        } else {
            OperationResultError("Cartão com o número $cardNumber não foi encontrado", HttpStatusCode.NotFound)
        }
    }

}

