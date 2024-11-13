package com.bburghouwt.compression.algorithms.lzw

import kotlin.math.ceil
import kotlin.math.ln

class LzwCompressor {
    private val initialDictSize = 256

    fun compress(input: String): Pair<List<Int>, Int> {
        val dictionary = mutableMapOf<String, Int>()
        var dictSize = initialDictSize

        initializeDict(dictionary)

        var current = ""
        val compressedData = mutableListOf<Int>()

        for (char in input) {
            val combined = current + char

            if (dictionary.containsKey(combined)) {
                current = combined
            } else {
                try {
                    compressedData.add(dictionary[current]!!)
                } catch (e: Exception) {
                    println("Failed at symbol: '$current'")
                    throw e
                }
                dictionary[combined] = dictSize++
                current = char.toString()
            }
        }

        compressedData.add(dictionary[current]!!)

        val bitWidth = calculateBitWidth(dictSize)

        return Pair(compressedData, bitWidth)
    }

    private fun initializeDict(dictionary: MutableMap<String, Int>) {
        for (i in 0 until initialDictSize) {
            dictionary[i.toChar().toString()] = i
        }
    }

    private fun calculateBitWidth(dictSize: Int): Int {
        return ceil(ln(dictSize.toDouble()) / ln(2.0)).toInt()
    }
}