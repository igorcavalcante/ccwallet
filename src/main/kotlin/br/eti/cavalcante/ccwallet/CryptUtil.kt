package br.eti.cavalcante.ccwallet

import org.apache.commons.codec.digest.DigestUtils
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

val algorithm = "AES"

fun digest(value: String) = org.apache.commons.codec.digest.DigestUtils.sha256Hex(value)

private fun genKey(key: String): SecretKey {
    val digest = DigestUtils.sha512Hex(key).take(16)
    return SecretKeySpec(digest.toByteArray(), algorithm)
}

fun enc(key: String, value: String): String {
    val c = Cipher.getInstance(algorithm)
    c.init(Cipher.ENCRYPT_MODE, genKey(key))
    return Base64.getEncoder().encodeToString(c.doFinal(value.toByteArray()))
}

fun dec(key: String, value: String): String {
    val c = Cipher.getInstance(algorithm)
    c.init(Cipher.DECRYPT_MODE, genKey(key))
    val valueBytes = Base64.getDecoder().decode(value)
    return String(c.doFinal(valueBytes))
}

