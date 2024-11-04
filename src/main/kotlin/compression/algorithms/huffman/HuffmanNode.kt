package com.bburghouwt.compression.algorithms.huffman

data class HuffmanNode(
    val char: Char? = null,
    val frequency: Int,
    val left: HuffmanNode? = null,
    val right: HuffmanNode? = null
) : Comparable<HuffmanNode> {
    override fun compareTo(
        other: HuffmanNode
    ): Int = this.frequency - other.frequency
}