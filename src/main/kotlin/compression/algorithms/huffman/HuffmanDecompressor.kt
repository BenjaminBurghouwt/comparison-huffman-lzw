package com.bburghouwt.compression.algorithms.huffman

class HuffmanDecompressor(private val huffmanCodes: Map<Char, String>) {

    fun decompress(compressed: String): String {
        val reverseCodes = huffmanCodes.entries.associate { it.value to it.key }

        var code = ""
        val decompressedData = StringBuilder()

        for (bit in compressed) {
            code += bit
            reverseCodes[code]?.let {
                decompressedData.append(it)
                code = ""
            }
        }
        return decompressedData.toString()
    }
}
