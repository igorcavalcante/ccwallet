package br.eti.cavalcante.ccwallet

import br.eti.cavalcante.ccwallet.model.CreditCard
import br.eti.cavalcante.ccwallet.model.User
import br.eti.cavalcante.ccwallet.model.Wallet
import io.ebean.Ebean
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal to`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should throw`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.math.BigDecimal
import java.math.BigDecimal.*
import java.time.LocalDate

object WalletSpek: Spek({

    beforeEachTest {
        Ebean.beginTransaction()
    }

    afterEachTest { Ebean.rollbackTransaction() }

    given("No Wallets") {

        on("Creating an empty wallet") {
            Wallet(
                    User("Igor Cavalcante", "igorrlc", "123"),
                    listOf(),
                    ZERO
            ).save()

            it("should create an empty wallet") {
                val wallet = Ebean.find(Wallet::class.java)
                        .where().eq("user.userName", "igorrlc")
                        .findOne()

                wallet!!.user.userName `should equal to` "igorrlc"
                wallet!!.cards.size `should equal to` 0
            }
        }

        on("Creating with two credit card") {
            Wallet(
                    User("Igor Cavalcante", "igorrlc", "123"),
                    listOf(
                            CreditCard("IGOR R L CAVALCANTE", "123", LocalDate.now(), 111, LocalDate.now(), TEN, TEN, ONE),
                            CreditCard("IGOR R L CAVALCANTE", "321", LocalDate.now(), 111, LocalDate.now(), TEN, TEN, ONE)
                    ),
                    TEN
            ).save()

            val wallet = Ebean.find(Wallet::class.java)
                    .where().eq("user.userName", "igorrlc")
                    .findOne()

            it("should create a wallet with two credit cards") {
                wallet!!.user.userName `should equal to` "igorrlc"
                wallet!!.cards.size `should equal to` 2
            }

            it("should have the correct limits") {
                wallet!!.cards.first().maxLimit `should be` TEN
            }
        }

        on("Creating with an existent user") {
            Wallet(
                    User("Igor Cavalcante", "igorrlc", "123"),
                    listOf(
                            CreditCard("IGOR R L CAVALCANTE", "123", LocalDate.now(), 111, LocalDate.now(), TEN, TEN, ONE),
                            CreditCard("IGOR R L CAVALCANTE", "321", LocalDate.now(), 111, LocalDate.now(), TEN, TEN, ONE)
                    ),
                    TEN
            ).save()

            it("should rise an exception") {
                val repeeated = Wallet(
                        User("Igor Cavalcante", "igorrlc", "123"),
                        listOf(),
                        ZERO
                )
                val functionException = { repeeated.save() }

                functionException `should throw` RuntimeException::class
            }
        }
    }

    given("A Wallet with 3 cards having 3 different due dates") {
        on("Purchasing with a value smaller than the smaller free limit") {
            val wallet = Wallet(
                    User("Igor Cavalcante", "igorrlc", "123"),
                    listOf(
                            CreditCard("CARD1", "123", LocalDate.now(), 111, LocalDate.now().plusDays(10), TEN, TEN, ONE),
                            CreditCard("CARD2", "321", LocalDate.now(), 111, LocalDate.now().plusDays(20), TEN, TEN, ONE),
                            CreditCard("CARD3", "456", LocalDate.now(), 111, LocalDate.now().plusDays(15), TEN, TEN, ONE)
                    ),
                    TEN
            )

            val result = wallet.purchase(BigDecimal.ONE)
            it("should return the correct PurchaseResult") {
                result.amount `should be` ONE
                result.entries.size `should equal to` 1
                result.entries.first().first `should be` ONE
                result.entries.first().second.usage `should equal`  BigDecimal("2")
            }

            it("should choose the card that's farther to pay") {
                val card1 = Ebean.find(CreditCard::class.java)
                    .where().eq("name", "CARD2")
                    .findOne()

                card1!!.usage `should equal` BigDecimal(2)
            }

        }

        on("Purchasing with a value greater than the free limit of the card with the farthest due date") {
            val wallet = Wallet(
                    User("Igor Cavalcante", "igorrlc", "123"),
                    listOf(
                        CreditCard("CARD1", "123", LocalDate.now(), 111, LocalDate.now().plusDays(10), TEN, TEN, ONE),
                        CreditCard("CARD2", "321", LocalDate.now(), 111, LocalDate.now().plusDays(20), TEN, TEN, ONE),
                        CreditCard("CARD3", "456", LocalDate.now(), 111, LocalDate.now().plusDays(15), BigDecimal("20"), BigDecimal("20"), ONE)
                    ),
                    BigDecimal("30")
            )

            val result = wallet.purchase(BigDecimal("15"))
            it("should return the correct PurchaseResult") {
                result.amount `should equal` BigDecimal("15")
                result.entries.size `should equal to` 1
                result.entries.first().first `should be` BigDecimal("15")
                result.entries.first().second.usage `should equal`  BigDecimal("16")
            }

            it("should choose the card that's farther to pay and having the available amount") {
                val card3 = Ebean.find(CreditCard::class.java)
                    .where().eq("name", "CARD3")
                    .findOne()

                card3!!.usage `should equal` BigDecimal("16")
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


})

/*
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
