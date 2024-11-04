package com.bburghouwt.compression.metrics

fun calculateCompressionRate(originalSizeBits: Int, compressedSizeBits: Int): Double {
    return  (compressedSizeBits / originalSizeBits.toDouble()) * 100
}

fun huffmanCompressionRate(input: String, compressedData: String): Double {
    val originalSizeBits = input.length * 8  // ASCII encoding
    val compressedSizeBits = compressedData.length  // Each bit in the encoded string
    return calculateCompressionRate(originalSizeBits, compressedSizeBits)
}

fun lzwCompressionRate(input: String, compressedData: List<Int>, bitWidth: Int): Double {
    val originalSizeBits = input.length * 8  // ASCII encoding
    val compressedSizeBits = compressedData.size * bitWidth  // Each dictionary index in bitWidth bits
    return calculateCompressionRate(originalSizeBits, compressedSizeBits)
}

inline fun <T> measureTimeMillis(block: () -> T): TimedResult<T> {
    val start = System.currentTimeMillis()
    val result = block()
    val end = System.currentTimeMillis()
    return TimedResult(result, end - start)
}

data class TimedResult<T>(val result: T, val time: Long)