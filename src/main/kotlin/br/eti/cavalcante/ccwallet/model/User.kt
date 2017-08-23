package br.eti.cavalcante.ccwallet.model

import javax.persistence.Column
import javax.persistence.Entity


@Entity
class User(
        val name: String,
        @Column(unique = true) var userName: String,
        val password: String
) : BaseModel()
