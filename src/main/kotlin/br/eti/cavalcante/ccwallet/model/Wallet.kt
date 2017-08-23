package br.eti.cavalcante.ccwallet.model

import java.math.BigDecimal
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
class Wallet(
        @OneToOne(cascade = arrayOf(CascadeType.ALL)) val user: User,
        @OneToMany(cascade = arrayOf(CascadeType.ALL)) val cards: List<CreditCard>,
        val realLimit: BigDecimal
) : BaseModel() {

    fun create() {

    }

    private fun getMaximunLimit() {

    }

}

