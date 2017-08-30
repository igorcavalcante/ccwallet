package br.eti.cavalcante.ccwallet.persist.data

import br.eti.cavalcante.ccwallet.persist.tables.Users
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class UserData(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<UserData>(Users)

    var name by Users.name
    var userName by Users.userName
    var password by Users.password

}
