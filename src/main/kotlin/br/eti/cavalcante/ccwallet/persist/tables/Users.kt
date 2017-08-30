package br.eti.cavalcante.ccwallet.persist.tables

import org.jetbrains.exposed.dao.IntIdTable


object Users : IntIdTable() {
    val name = varchar("name", 50)
    val userName = varchar("userName", 10).uniqueIndex()
    val password = varchar("password", 100)
}