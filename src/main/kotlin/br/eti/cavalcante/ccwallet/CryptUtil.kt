package br.eti.cavalcante.ccwallet

import org.apache.commons.codec.digest.DigestUtils
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object CryptUtil {
    val algorithm = "AES"
    private lateinit var key: String

    fun init(key: String): CryptUtil {
        this.key = key
        return this
    }

    fun digest() = org.apache.commons.codec.digest.DigestUtils.sha256Hex(key)

    private fun genKey(): SecretKey {
        val digest = DigestUtils.sha512Hex(key).take(16)
        return SecretKeySpec(digest.toByteArray(), algorithm)
    }

    fun enc(value: String): String {
        val c = Cipher.getInstance(algorithm)
        c.init(Cipher.ENCRYPT_MODE, genKey())
        return Base64.getEncoder().encodeToString(c.doFinal(value.toByteArray()))
    }

    fun dec(value: String): String {
        val c = Cipher.getInstance(algorithm)
        c.init(Cipher.DECRYPT_MODE, genKey())
        val valueBytes = Base64.getDecoder().decode(value)
        return String(c.doFinal(valueBytes))
    }

}

