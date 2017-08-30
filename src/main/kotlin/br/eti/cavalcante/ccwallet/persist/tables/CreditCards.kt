package br.eti.cavalcante.ccwallet.persist.tables

import org.jetbrains.exposed.dao.IntIdTable


object CreditCards : IntIdTable() {
    val name = varchar("name", 50)
    val number = varchar("number", 50)
    val expirationDate = date("expiration_date")
    val securityNumber = integer("security_number")
    val dueDate = date("duedate")
    val userLimit = decimal("user_limit", 10, 2)
    val maxLimit = decimal("max_limit", 10, 2)
    val usage = decimal("usage", 10, 2)

    val wallet = reference("wallet", Wallets)
}