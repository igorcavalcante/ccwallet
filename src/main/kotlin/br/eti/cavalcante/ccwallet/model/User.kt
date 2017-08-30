package br.eti.cavalcante.ccwallet.model

import javax.persistence.Entity

@Entity
class User() : BaseModel() {

    var name:  String = ""
    var userName: String = ""
    var password: String = ""

    constructor(name: String, userName: String, password: String) : this() {
        this.name = name
        this.userName = userName
        this.password = password
    }
}
