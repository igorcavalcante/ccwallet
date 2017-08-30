package br.eti.cavalcante.ccwallet

import br.eti.cavalcante.ccwallet.model.User
import br.eti.cavalcante.ccwallet.model.Wallet
import br.eti.cavalcante.ccwallet.persist.data.UserData
import br.eti.cavalcante.ccwallet.persist.data.WalletData
import br.eti.cavalcante.ccwallet.persist.tables.CreditCards
import br.eti.cavalcante.ccwallet.persist.tables.Users
import br.eti.cavalcante.ccwallet.persist.tables.Wallets
import org.amshove.kluent.`should equal to`
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.math.BigDecimal

object WalletSpek: Spek({

    given("No Wallets") {
        Database.connect(
                "jdbc:postgresql://localhost/ccwallet_test",
                "org.postgresql.Driver",
                "ccwallet",
                "ccwallet"
        )

        beforeEachTest { transaction { create(Users, CreditCards, Wallets) } }
        afterEachTest { transaction { drop(Users, CreditCards, Wallets) } }

        on("Creating an empty wallet") {
            transaction {
                Wallet(
                        User("Igor Cavalcante", "igorrlc", "123"),
                        listOf(),
                        BigDecimal.ZERO
                ).save()

            }

            it("should create an empty wallet") {
                transaction { WalletData.all().first().cards.count() `should equal to` 0 }
            }

            it("should create an user") {
                transaction { UserData.all().first().name `should equal to` "Igor Cavalcante" }
            }

            it("maxLimit should be 0") {
                transaction { WalletData.all().first(). `should equal to` 0 }
            }
        }
    }

    on("Creating with a credit card") {
    transaction {
        create(Users, Wallets, CreditCards)

        it("should create a wallet with two credit cards") {

        }

        it("should create an user") {

        }

    }
}
    /*

        on("Creating with an existent user") {

            it("should not create the wallet") {

            }

        }

    }

    given("A Wallets with 3 cards having 3 different due dates") {

        on("Purchasing") {

            it("should choose the cart that's farther to pay") {

            }

        }

        on("Purchasing an amount greater than the largest remaining limit") {

            it("must divide the purchase into more cards") {

            }

        }

        on("Changing credit limit to less than sum of the limit of the cards") {

            it("should allow the change") {

            }

        }

        on("Changing credit limit to the same value than the sum of the limit of the cards") {

            it("should allow the change") {

            }

        }

        on("Changing credit limit to more than the sum of the limit of the cards") {

            it("should deny the change") {

            }

        }

        on("removing a card") {

            it("should decrease the number of cards") {

            }

            it("should decrease the maximum limit") {

            }

        }

        on("removing a card and your real limit being fewer than you maximum limit") {

            it("should adjust your real limit to the real limit value and emit a message") {

            }

        }

        on("fetching the card information") {
            it("should be able to show all information of the wallet (including real limit, maximum limit and avaiable credit)") {

            }
        }

    }

    given("A Wallets with 2 cards with the same due date") {

        on("Purchasing with a amount smaller than the smallest remaining limit") {

            it("should choose the one with the smallest remaining limit") {
            }

        }

        on("Purchasing with a amount greater than the smallest remaining limit") {

            it("should choose the one with the greater remaining limit") {
            }

        }

    }
*/

})