package br.eti.cavalcante.ccwallet

import br.eti.cavalcante.ccwallet.model.User
import org.amshove.kluent.`should equal to`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class UserSpek : Spek({

    given("A user") {
        on("testomg") {
            User("Igor", "igor", "teste").save()
            it(" test") {
/*                val user = QUser().name.eq("Igor").findOne()
                user!!.userName `should equal to` "igor"*/
            }
        }
    }

})

