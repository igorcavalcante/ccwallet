package br.eti.cavalcante.ccwallet

import org.amshove.kluent.`should equal to`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class CryptUtilSpek: Spek({

    describe("A CryptUtil") {

        on("Encypting") {

            val encoded = CryptUtil.init("123456789").enc( "111222333444")

            it("should transform data in a reversible way") {
                CryptUtil.dec( encoded) `should equal to` "1112223334441"
            }
        }

    }
})