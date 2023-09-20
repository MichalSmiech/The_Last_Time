package com.michasoft.thelasttime.util

import java.security.SecureRandom
import java.util.Random

/**
 * Created by mśmiech on 11.11.2021.
 */
object IdGenerator {
    private const val AUTO_ID_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    private const val AUTO_ID_LENGTH = 20
    private val rand: Random = SecureRandom()

    fun newId(): String {
        val builder = StringBuilder()
        val maxRandom = AUTO_ID_ALPHABET.length
        for (i in 0 until AUTO_ID_LENGTH) {
            builder.append(AUTO_ID_ALPHABET[rand.nextInt(maxRandom)])
        }
        return builder.toString()
    }
}