package br.eti.cavalcante.ccwallet.model

import javax.persistence.Column
import javax.persistence.Entity

@Entity(name="User")
data class User(
    val name: String,
    @Column(unique = true)
    val userName: String,
    val password: String
) : BaseModel()
