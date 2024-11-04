package com.bburghouwt.compression.algorithms.huffman

import java.util.PriorityQueue

class HuffmanCompressor {

    private val huffmanCodes = mutableMapOf<Char, String>()

    fun compress(input: String): String {
        val frequencyMap = input.groupingBy { it }.eachCount()

        val root = buildHuffmanTree(frequencyMap)
        generateHuffmanCodes(root, "")

        return input.map { huffmanCodes[it] }.joinToString("")
    }

    private fun buildHuffmanTree(frequencyMap: Map<Char, Int>): HuffmanNode {
        val priorityQueue = PriorityQueue<HuffmanNode>().apply {
            frequencyMap.forEach { (char, freq) -> add(HuffmanNode(char, freq)) }
        }

        while (priorityQueue.size > 1) {
            val left = priorityQueue.poll()
            val right = priorityQueue.poll()
            priorityQueue.add(HuffmanNode(null, left.frequency + right.frequency, left, right))
        }

        return priorityQueue.poll()
    }

    private fun generateHuffmanCodes(node: HuffmanNode?, code: String) {
        if (node == null) return

        if (node.char != null) {
            huffmanCodes[node.char] = code
        }

        generateHuffmanCodes(node.left, code + "0")
        generateHuffmanCodes(node.right, code + "1")
    }

    fun getHuffmanCodes(): Map<Char, String> = huffmanCodes
}