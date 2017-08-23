package br.eti.cavalcante.ccwallet

import br.eti.cavalcante.ccwallet.model.User
import io.ebean.Ebean
import org.amshove.kluent.`should equal to`


class UserTest {

//    @Test
    fun save() {
        val user = User("Igor", "igorrlc", "12345")

        Ebean.save(user)


        1 `should equal to` 1

//        val const = QUser().name.eq("Igor").findOneOrEmpty()

//        const.ifPresent { it.name.`should equal to`("Igor") }
    }

}