package br.eti.cavalcante.ccwallet.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.ebean.Ebean
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity(name="User")
data class User(
    val name: String,
    @Column(unique = true) val userName: String,
    var password: String
) : BaseModel() {

    @OneToOne @JsonIgnore lateinit var wallet: Wallet

    companion object {
        fun auth(userName: String, password: String): User? {
            val user = Ebean.find(User::class.java).where().eq("userName", userName).findOne()
            return if(user!= null && user.password == password)  user
            else null
        }
    }
}
