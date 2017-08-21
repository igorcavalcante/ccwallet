package br.eti.cavalcante.ccwallet.model

import io.ebean.Model
import javax.persistence.Entity
import javax.persistence.Id


@Entity
class User(val name: String) : Model() {

    @Id
    var id: Long = 0

    var version: Long = 0

}
