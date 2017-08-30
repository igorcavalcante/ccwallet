package br.eti.cavalcante.ccwallet.persist.data

import br.eti.cavalcante.ccwallet.persist.tables.CreditCards
import br.eti.cavalcante.ccwallet.persist.tables.Wallets
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class WalletData(id: EntityID<Int>): IntEntity(id) {
    var user by UserData referencedOn Wallets.user
    val cards by CreditCardData referrersOn CreditCards.wallet
    var realLimit by Wallets.realLimit

    companion object : IntEntityClass<WalletData>(Wallets)
}

