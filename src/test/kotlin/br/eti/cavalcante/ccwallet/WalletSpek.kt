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
                wallet.cards.size `should equal to` 0
            }
        }

        on("Creating with two credit card") {
            Wallet(
                    User("Igor Cavalcante", "igorrlc", "123"),
                    listOf(
                            CreditCard("IGOR R L CAVALCANTE", "123", LocalDate.now(), 111, LocalDate.now(), TEN, ONE),
                            CreditCard("IGOR R L CAVALCANTE", "321", LocalDate.now(), 111, LocalDate.now(), TEN, ONE)
                    ),
                    TEN
            ).save()

            val wallet = Ebean.find(Wallet::class.java)
                    .where().eq("user.userName", "igorrlc")
                    .findOne()

            it("should create a wallet with two credit cards") {
                wallet!!.user.userName `should equal to` "igorrlc"
                wallet.cards.size `should equal to` 2
            }

            it("should have the correct limits") {
                wallet!!.cards.first().cardLimit `should be` TEN
            }
        }

        on("Creating with an existent user") {
            Wallet(
                    User("Igor Cavalcante", "igorrlc", "123"),
                    listOf(
                            CreditCard("IGOR R L CAVALCANTE", "123", LocalDate.now(), 111, LocalDate.now(), TEN, ONE),
                            CreditCard("IGOR R L CAVALCANTE", "321", LocalDate.now(), 111, LocalDate.now(), TEN, ONE)
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


    given("A Wallet with 3 cards, two with the same dueDate and one with a diferent one") {
        on("Sorting") {
            val ordered = Wallet(
                User("Igor Cavalcante", "igorrlc", "123"),
                listOf(
                    CreditCard("CARD1", "123", LocalDate.now(), 111, LocalDate.now().plusDays(10), TEN, ONE),
                    CreditCard("CARD2", "321", LocalDate.now(), 111, LocalDate.now().plusDays(10), TEN, BigDecimal("6")),
                    CreditCard("CARD3", "456", LocalDate.now(), 111, LocalDate.now().plusDays(15), TEN, BigDecimal("5"))
                ),
                TEN
            ).sortedCards()

            it("should sort descending by dueDate and ascending by free available amount") {
                ordered.first().name `should equal to` "CARD3"
                ordered[1].name `should equal to` "CARD2"
                ordered.last().name `should equal to` "CARD1"
            }

        }
    }

    given("A Wallet with 3 cards having 3 different due dates") {
        val wallet3Cards = {
            Wallet(
                User("Igor Cavalcante", "igorrlc", "123"),
                listOf(
                    CreditCard("CARD1", "123", LocalDate.now(), 111, LocalDate.now().plusDays(10), TEN, ONE),
                    CreditCard("CARD2", "321", LocalDate.now(), 111, LocalDate.now().plusDays(20), TEN, ONE),
                    CreditCard("CARD3", "456", LocalDate.now(), 111, LocalDate.now().plusDays(15), BigDecimal("20"), ONE)
                ),
                BigDecimal("20")
            )
        }

        on("Purchasing with a value smaller than the smaller free limit") {
            val wallet = wallet3Cards()

            val result = wallet.purchase(BigDecimal.ONE)
            it("should return the correct PurchaseResult") {
                result.amount `should be` ONE
                result.entries.size `should equal to` 1
                result.entries.first().amountPaid `should be` ONE
                result.entries.first().card.name `should equal to` "CARD2"
                result.entries.first().card.usage `should equal`  BigDecimal("2")
            }

            it("should choose the card that's farther to pay") {
                val card2 = Ebean.find(CreditCard::class.java)
                    .where().eq("name", "CARD2")
                    .findOne()

                card2!!.usage `should equal` BigDecimal(2)
            }

        }

        on("Purchasing with a value greater than the free limit of the card with the farthest due date") {
            val wallet = wallet3Cards()

            val result = wallet.purchase(BigDecimal("15"))
            it("should return the correct PurchaseResult") {
                result.amount `should equal` BigDecimal("15")
                result.entries.size `should equal to` 1
                result.entries.first().amountPaid `should equal` BigDecimal("15")
                result.entries.first().card.usage `should equal`  BigDecimal("16")
            }

            it("should choose the card that's farther to pay and having the available amount") {
                val card3 = Ebean.find(CreditCard::class.java)
                    .where().eq("name", "CARD3")
                    .findOne()

                card3!!.usage `should equal` BigDecimal("16")
            }

        }

        on("Purchasing an amount greater than the largest remaining limit") {
            val wallet = wallet3Cards()
            val result = wallet.purchase(BigDecimal("20"))

            it("must divide the purchase into more cards") {
                result.amount `should equal` BigDecimal("20")
                result.entries.size `should equal to` 2
                result.entries.first().card.name `should equal to` "CARD2"
                result.entries.last().card.name `should equal to` "CARD3"
            }

            it("should update the usage values of the cards") {
                val card1 = Ebean.find(CreditCard::class.java)
                    .where().eq("name", "CARD1").findOne()
                card1!!.usage `should equal` ONE

                val card2 = Ebean.find(CreditCard::class.java)
                    .where().eq("name", "CARD2").findOne()
                card2!!.usage `should equal` BigDecimal("10")

                val card3 = Ebean.find(CreditCard::class.java)
                    .where().eq("name", "CARD3").findOne()
                card3!!.usage `should equal` BigDecimal("12")
            }

        }

        on("Purchasing an amount greater than wallet credit available") {
            val wallet = wallet3Cards()
            val result = wallet.purchase(BigDecimal("100.00"))

            it("should not complete the transaction") {
                result.success `should equal to` false
                result.amount `should equal` ZERO
            }
        }

        on("Changing credit limit to less than sum of the limit of the cards") {
            val wallet = wallet3Cards()
            val result = wallet.updateLimit(BigDecimal("21"))

            it("should allow the change") {
                result.success `should equal to` true
                wallet.userLimit `should equal` BigDecimal(21)
            }

        }

        on("Changing credit limit to more than the sum of the limit of the cards") {
            val wallet = wallet3Cards()
            val result = wallet.updateLimit(BigDecimal("1000"))

            it("should deny the change") {
                result.success `should equal` false
                wallet.userLimit `should equal` BigDecimal("20")
            }
        }

        on("removing a card") {
            val wallet = wallet3Cards()
            wallet.save()
            wallet.removeCard("123")

            it("should decrease the number of cards") {
               wallet.cards.size `should equal to` 2
            }

            it("should delete the card from database") {
                val count = Ebean.find(CreditCard::class.java).findCount()
                count `should equal to` 2
            }
        }

        on("removing a non existent card") {
            val wallet = wallet3Cards()
            val result = wallet.removeCard("999")

            it("should return an error") {
                result.success `should equal to` false
            }

            it("should not alter the number of cards") {
                wallet.cards.size `should equal to` 3
            }
        }

        on("removing a card and your user limit being fewer than you maximum limit") {
            val wallet = wallet3Cards()
            wallet.updateLimit(BigDecimal("40"))
            val result = wallet.removeCard("456")

            it("should return not an error") {
                result.success `should equal to` true
            }

            it("should alter the number of cards") {
                wallet.cards.size `should equal to` 2
            }

            it("should adjust your real limit to the real limit value and emit a message") {
                wallet.userLimit `should equal` BigDecimal("20")
            }

        }

    }

    given("Wallet with one card") {
        val wallet = Wallet(
            User("Igor Cavalcante", "igorrlc", "123"),
            listOf(
                CreditCard("TESTE", "123", LocalDate.of(2099, 9, 1), 111, LocalDate.of(2017, 9, 1), TEN, ONE)
            ),
            BigDecimal("10")
        )

        on("paying the invoices") {
            wallet.payInvoice("123")
            it("should alter the paymentDate to + 1 month") {
                wallet.cards.first().dueDate `should equal` LocalDate.of(2017, 10, 1)
            }
        }
    }

})

