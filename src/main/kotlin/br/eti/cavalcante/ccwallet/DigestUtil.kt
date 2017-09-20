package br.eti.cavalcante.ccwallet

fun digest(value: String) = org.apache.commons.codec.digest.DigestUtils.sha256Hex(value)

