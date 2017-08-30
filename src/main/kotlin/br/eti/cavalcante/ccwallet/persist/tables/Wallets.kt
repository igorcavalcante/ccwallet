package br.eti.cavalcante.ccwallet.persist.tables

import org.jetbrains.exposed.dao.IntIdTable


object Wallets : IntIdTable() {
    val realLimit = decimal("real_limit", 10, 2)
    val user = reference("user", Users)
}