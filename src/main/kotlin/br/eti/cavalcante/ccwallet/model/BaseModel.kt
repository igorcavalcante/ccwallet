package br.eti.cavalcante.ccwallet.model

import io.ebean.Model
import io.ebean.annotation.WhenCreated
import io.ebean.annotation.WhenModified
import java.security.Timestamp
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Version


@MappedSuperclass
open class BaseModel : Model() {

  @Id
  var id: Long? = null

  @Version
  var version: Long? = null

  @WhenCreated
  var whenCreated: Timestamp? = null

  @WhenModified
  var whenModified: Timestamp? = null

}