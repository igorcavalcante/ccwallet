package br.eti.cavalcante.ccwallet.persist.data

import br.eti.cavalcante.ccwallet.persist.tables.CreditCards
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class CreditCardData(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<CreditCardData>(CreditCards)

    var name by CreditCards.name
    var number by CreditCards.number
    var expirationDate by CreditCards.expirationDate
    var securityNumber by CreditCards.securityNumber
    var dueDate by CreditCards.dueDate
    var userLimit by CreditCards.userLimit
    var maxLimit by CreditCards.maxLimit
    var usage by CreditCards.usage

    fun purchase() {

    }

    fun pay() {

    }

}