package com.bburghouwt

import com.bburghouwt.compression.algorithms.huffman.HuffmanCompressor
import com.bburghouwt.compression.algorithms.huffman.HuffmanDecompressor
import com.bburghouwt.compression.algorithms.lzw.LzwCompressor
import com.bburghouwt.compression.algorithms.lzw.LzwDecompressor
import com.bburghouwt.compression.metrics.ReportPrinter
import com.bburghouwt.compression.metrics.measureTimeMillis
import com.bburghouwt.compression.getTextFiles
import com.bburghouwt.compression.readTextFile
import java.lang.management.ManagementFactory
import com.sun.management.OperatingSystemMXBean

fun compressionComparison(printSummary: Boolean, printAllReports: Boolean, printSummaryForSheets: Boolean) {
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
    val reportPrinter = ReportPrinter()

    inputFiles.forEach { filePath ->
        val inputText = readTextFile(filePath)

        val (lzwMemoryUsage, lzwCpuUsage) = measureResourceUsage {
            val lzwCompressor = LzwCompressor()
            lzwCompressor.compress(inputText)
        }

        val (huffmanMemoryUsage, huffmanCpuUsage) = measureResourceUsage {
            val huffmanCompressor = HuffmanCompressor()
            huffmanCompressor.compress(inputText)
        }

        reportPrinter.appendResourceUsage(inputText, Pair(lzwMemoryUsage, lzwCpuUsage), Pair(huffmanMemoryUsage, huffmanCpuUsage))
    }

    reportPrinter.printResourceUsageTable()
}

private fun measureResourceUsage(block: () -> Unit): Pair<Long, Double> {
    val runtime = Runtime.getRuntime()
    val osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean::class.java) as OperatingSystemMXBean

    val memoryBefore = runtime.totalMemory() - runtime.freeMemory()
    val cpuLoadBefore = osBean.processCpuLoad

    block()

    val memoryAfter = runtime.totalMemory() - runtime.freeMemory()
    val cpuLoadAfter = osBean.processCpuLoad

    val memoryUsage = memoryAfter - memoryBefore
    val cpuUsagePercentage = (cpuLoadAfter - cpuLoadBefore) * 100

    return Pair(memoryUsage, cpuUsagePercentage)
}