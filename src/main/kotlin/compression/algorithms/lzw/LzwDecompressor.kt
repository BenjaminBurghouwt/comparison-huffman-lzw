package com.bburghouwt.compression.algorithms.lzw

class LzwDecompressor {

    private val initialDictSize = 256

    fun decompress(compressed: List<Int>): String {
        val dictionary = mutableMapOf<Int, String>()
        var dictSize = initialDictSize

        initializeDict(dictionary)

        var previous = dictionary[compressed[0]]!!
        val decompressedData = StringBuilder(previous)

        for (i in 1 until compressed.size) {
            val code = compressed[i]
            val entry = dictionary[code] ?: (previous + previous[0])

            decompressedData.append(entry)

            dictionary[dictSize++] = previous + entry[0]
            previous = entry
        }

        return decompressedData.toString()
    }

    private fun initializeDict(dictionary: MutableMap<Int, String>) {
        for (i in 0 until initialDictSize) {
            dictionary[i] = i.toChar().toString()
        }
    }
}