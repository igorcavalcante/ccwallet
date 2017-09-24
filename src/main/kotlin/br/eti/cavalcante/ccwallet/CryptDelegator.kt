package br.eti.cavalcante.ccwallet

import br.eti.cavalcante.ccwallet.model.CreditCard
import kotlin.reflect.KProperty

class CryptDelegator {

    operator fun setValue(creditCard: CreditCard, property: KProperty<*>, s: String?) {
        if(creditCard.name == null || creditCard.number == null || creditCard.securityNumber == null) {
            creditCard.decFields()
        }
    }

    operator fun getValue(creditCard: CreditCard, property: KProperty<*>): String? {
        if(creditCard.cryptName == null || creditCard.cryptNumber == null || creditCard.cryptSecurityNumber == null) {
            creditCard.encFields()
        }
        return property.getter.call().toString()
    }
}