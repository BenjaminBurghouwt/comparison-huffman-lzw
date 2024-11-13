package com.bburghouwt

import com.bburghouwt.compression.algorithms.huffman.HuffmanCompressor
import com.bburghouwt.compression.algorithms.huffman.HuffmanDecompressor
import com.bburghouwt.compression.algorithms.lzw.LzwCompressor
import com.bburghouwt.compression.algorithms.lzw.LzwDecompressor
import com.bburghouwt.compression.metrics.GlobalReportPrinter
import com.bburghouwt.compression.metrics.ResourceUsagePrinter
import com.bburghouwt.compression.getTextFiles
import com.bburghouwt.compression.metrics.CompressionMetrics
import com.bburghouwt.compression.readTextFile

private val metrics = CompressionMetrics()

fun compressionComparison(printSummary: Boolean, printAllReports: Boolean, printSummaryForSheets: Boolean) {

    val inputFiles = getTextFiles("src/main/resources/texts")
    val reportPrinter = GlobalReportPrinter()

    inputFiles.forEach { filePath ->
        val inputText = readTextFile(filePath)

        val lzwCompressor = LzwCompressor()
        val lzwCompression = metrics.measureTimeMillis { lzwCompressor.compress(inputText) }
        val (lzwCompressedData, lzwBitWidth) = lzwCompression.result
        val lzwDecompressor = LzwDecompressor()
        val lzwDecompressedData = metrics.measureTimeMillis { lzwDecompressor.decompress(lzwCompressedData) }

        val huffmanCompressor = HuffmanCompressor()
        val huffmanCompressedData = metrics.measureTimeMillis { huffmanCompressor.compress(inputText) }
        val huffmanDecompressor = HuffmanDecompressor(huffmanCompressor.getHuffmanCodes())
        val huffmanDecompressedData = metrics.measureTimeMillis { huffmanDecompressor.decompress(huffmanCompressedData.result) }

        reportPrinter.appendToReport(
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

    if (printSummary) {
        reportPrinter.printSummary()
    }
    if (printAllReports) {
        reportPrinter.printAllReports()
    }
    if (printSummaryForSheets) {
        reportPrinter.printSummaryForSheets()
    }
}

fun compressionResourceUsage() {
    val inputFiles = getTextFiles("src/main/resources/texts")
    val resourceUsagePrinter = ResourceUsagePrinter()

    inputFiles.forEach { filePath ->
        val inputText = readTextFile(filePath)

        val (lzwMemoryUsage, lzwCpuUsage) = metrics.measureResourceUsage {
            val lzwCompressor = LzwCompressor()
            lzwCompressor.compress(inputText)
        }

        val (huffmanMemoryUsage, huffmanCpuUsage) = metrics.measureResourceUsage {
            val huffmanCompressor = HuffmanCompressor()
            huffmanCompressor.compress(inputText)
        }

        resourceUsagePrinter.appendResourceUsage(inputText, Pair(lzwMemoryUsage, lzwCpuUsage), Pair(huffmanMemoryUsage, huffmanCpuUsage))
    }

    resourceUsagePrinter.printResourceUsageTable()
}