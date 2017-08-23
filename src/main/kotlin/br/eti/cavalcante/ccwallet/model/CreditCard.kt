package br.eti.cavalcante.ccwallet.model

import io.ebean.annotation.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.Entity

@Entity
class CreditCard (
    val name: String,
    val number: String,
    val expirationDate: LocalDate,
    val securityNumber: Int,
    val dueDate: LocalDate,
    val userLimit: BigDecimal,
    val maxLimit: BigDecimal,
    val usage: BigDecimal
) : BaseModel() {

    fun purchase() {

    }

    fun pay() {

    }

}