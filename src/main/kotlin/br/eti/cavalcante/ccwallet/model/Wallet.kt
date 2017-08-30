package br.eti.cavalcante.ccwallet.model

import br.eti.cavalcante.ccwallet.exceptions.ValidationException
import br.eti.cavalcante.ccwallet.persist.data.UserData
import br.eti.cavalcante.ccwallet.persist.data.WalletData
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class Wallet(
    val user: User,
    val cards: List<CreditCard>,
    val userLimit: BigDecimal
) {

    fun save(): Wallet {
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
    }

    fun calculateMaxLimit() = cards.fold(ZERO) { acc, card -> acc + card.maxLimit }

}

