package com.bburghouwt

import com.bburghouwt.compression.algorithms.huffman.HuffmanCompressor
import com.bburghouwt.compression.algorithms.huffman.HuffmanDecompressor
import com.bburghouwt.compression.algorithms.lzw.LzwCompressor
import com.bburghouwt.compression.algorithms.lzw.LzwDecompressor
import com.bburghouwt.compression.metrics.ReportPrinter
import com.bburghouwt.compression.metrics.measureTimeMillis
import com.bburghouwt.compression.getTextFiles
import com.bburghouwt.compression.readTextFile

fun main() {
    val inputFiles = getTextFiles("src/main/resources/texts")
    val reportPrinter = ReportPrinter()

    inputFiles.forEach { filePath ->
        val inputText = readTextFile(filePath)

        val lzwCompressor = LzwCompressor()
        val lzwCompression = measureTimeMillis { lzwCompressor.compress(inputText) }
        val (lzwCompressedData, lzwBitWidth) = lzwCompression.result
        val lzwDecompressor = LzwDecompressor()
        val lzwDecompressedData = measureTimeMillis { lzwDecompressor.decompress(lzwCompressedData) }

        val huffmanCompressor = HuffmanCompressor()
        val huffmanCompressedData = measureTimeMillis { huffmanCompressor.compress(inputText) }
        val huffmanDecompressor = HuffmanDecompressor(huffmanCompressor.getHuffmanCodes())
        val huffmanDecompressedData = measureTimeMillis { huffmanDecompressor.decompress(huffmanCompressedData.result) }

        reportPrinter.printReport(
            inputText,
            lzwCompressedData,
            lzwBitWidth,
            lzwCompression.time,
            lzwDecompressedData.time,
            lzwDecompressedData.result,
            huffmanCompressedData.result,
            huffmanCompressedData.time,
            huffmanDecompressedData.time,
            huffmanDecompressedData.result,
            huffmanCompressor.getHuffmanCodes()
        )
    }

    reportPrinter.printSummary()
//    reportPrinter.printAllReports()
//    reportPrinter.printSummaryForSheets()
}