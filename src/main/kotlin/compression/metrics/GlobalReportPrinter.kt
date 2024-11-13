package com.bburghouwt.compression.metrics

class GlobalReportPrinter {

    private val metrics = CompressionMetrics()

    private val allReports = mutableListOf<String>()
    private val summary = mutableListOf<GlobalSummary.SummaryEntry>()

    fun appendToReport(
        inputText: String,
        lzwCompressedData: List<Int>,
        lzwBitWidth: Int,
        lzwCompressionTime: Long,
        lzwDecompressionTime: Long,
        lzwDecompressedData: String,
        huffmanCompressedData: String,
        huffmanCompressionTime: Long,
        huffmanDecompressionTime: Long,
        huffmanDecompressedData: String,
        huffmanCodes: Map<Char, String>
    ) {
        val lzwCompressionRatio = metrics.lzwCompressionRate(inputText, lzwCompressedData, lzwBitWidth)
        val huffmanCompressionRatio = metrics.huffmanCompressionRate(inputText, huffmanCompressedData)

        val report = StringBuilder().apply {
            appendLine("========== Report for Input Size: ${metrics.formatSize(inputText.length)} ==========")
            appendLine()

            appendLine("LZW Compression Results")
            appendLine("-----------------------")
            appendLine("Compression Ratio: ${metrics.formatRatio(lzwCompressionRatio)}")
            appendLine("Compression Time: ${metrics.formatTime(lzwCompressionTime)}")
            appendLine("Decompression Time: ${metrics.formatTime(lzwDecompressionTime)}")
            appendLine("Dictionary Size: ${metrics.calculateLzwDictSize(lzwCompressedData)} entries")
            appendLine("Lossless: ${metrics.isLossless(inputText, lzwDecompressedData)}")
            appendLine()

            appendLine("Huffman Compression Results")
            appendLine("---------------------------")
            appendLine("Compression Ratio: ${metrics.formatRatio(huffmanCompressionRatio)}")
            appendLine("Compression Time: ${metrics.formatTime(huffmanCompressionTime)}")
            appendLine("Decompression Time: ${metrics.formatTime(huffmanDecompressionTime)}")
            appendLine("Unique Symbols: ${huffmanCodes.size}")
            appendLine("Lossless: ${metrics.isLossless(inputText, huffmanDecompressedData)}")
            appendLine("===============================================")
            appendLine()
        }

        allReports.add(report.toString())

        println("\u001B[32mCompressed file: ${metrics.formatSize(inputText.length)}\u001B[0m")

        summary.add(
            GlobalSummary.SummaryEntry(
                fileSize = inputText.length,
                lzwCompressionRate = lzwCompressionRatio,
                lzwCompressionTime = lzwCompressionTime,
                lzwDecompressionTime = lzwDecompressionTime,
                huffmanCompressionRate = huffmanCompressionRatio,
                huffmanCompressionTime = huffmanCompressionTime,
                huffmanDecompressionTime = huffmanDecompressionTime,
            )
        )
    }

    fun printAllReports() {
        allReports.forEach { println(it) }
    }

    fun printSummary() {
        GlobalSummary.printSummary(summary)
    }

    fun printSummaryForSheets() {
        GlobalSummary.printSummaryForSheets(summary)
    }
}