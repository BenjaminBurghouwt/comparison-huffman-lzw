package com.bburghouwt.compression.metrics

class ReportPrinter {

    private val allReports = mutableListOf<String>()
    private val summary = mutableListOf<SummaryEntry>()

    data class SummaryEntry(
        val fileSize: Int,
        val lzwCompressionRate: Double,
        val lzwCompressionTime: Long,
        val lzwDecompressionTime: Long,
        val huffmanCompressionRate: Double,
        val huffmanCompressionTime: Long,
        val huffmanDecompressionTime: Long,
    )

    fun printReport(
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
        val lzwCompressionRatio = lzwCompressionRate(inputText, lzwCompressedData, lzwBitWidth)
        val huffmanCompressionRatio = huffmanCompressionRate(inputText, huffmanCompressedData)

        val report = StringBuilder().apply {
            appendLine("========== Report for Input Size: ${formatSize(inputText.length)} ==========")
            appendLine()

            appendLine("LZW Compression Results")
            appendLine("-----------------------")
            appendLine("Compression Ratio: ${formatRatio(lzwCompressionRatio)}")
            appendLine("Compression Time: ${formatTime(lzwCompressionTime)}")
            appendLine("Decompression Time: ${formatTime(lzwDecompressionTime)}")
            appendLine("Dictionary Size: ${calculateLzwDictSize(lzwCompressedData)} entries")
            appendLine("Lossless: ${isLossless(inputText, lzwDecompressedData)}")
            appendLine()

            appendLine("Huffman Compression Results")
            appendLine("---------------------------")
            appendLine("Compression Ratio: ${formatRatio(huffmanCompressionRatio)}")
            appendLine("Compression Time: ${formatTime(huffmanCompressionTime)}")
            appendLine("Decompression Time: ${formatTime(huffmanDecompressionTime)}")
            appendLine("Unique Symbols: ${huffmanCodes.size}")
            appendLine("Lossless: ${isLossless(inputText, huffmanDecompressedData)}")
            appendLine("===============================================")
            appendLine()
        }

        allReports.add(report.toString())
        println("\u001B[32mCompressed file: ${formatSize(inputText.length)}\u001B[0m")

        summary.add(
            SummaryEntry(
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
        val sortedSummary = summary.sortedBy { it.fileSize }
        val summaryReport = StringBuilder()
        summaryReport.appendLine("=== Compression Summary Table ===")
        summaryReport.appendLine(
            String.format(
                "%-15s | %-15s | %-15s | %-18s | %-18s | %-18s | %-18s",
                "File Size", "LZW Comp. Rate", "LZW Comp. Time", "LZW Decomp. Time",
                "Huffman Comp. Rate", "Huffman Comp. Time", "Huffman Decomp. Time"
            )
        )
        summaryReport.appendLine("-".repeat(140))

        for (entry in sortedSummary) {
            summaryReport.appendLine(
                String.format(
                    "%-15s | %-15s | %-12s ms | %-15s ms | %-18s | %-15s ms | %-15s ms",
                    formatSize(entry.fileSize),
                    formatRatio(entry.lzwCompressionRate),
                    entry.lzwCompressionTime,
                    entry.lzwDecompressionTime,
                    formatRatio(entry.huffmanCompressionRate),
                    entry.huffmanCompressionTime,
                    entry.huffmanDecompressionTime
                )
            )
        }
        println(summaryReport.toString())
    }

    fun printSummaryForSheets() {
        val sortedSummary = summary.sortedBy { it.fileSize }
        val summaryReport = StringBuilder()

        summaryReport.appendLine(
            "File Size\tLZW Comp. Rate\tLZW Comp. Time (ms)\tLZW Decomp. Time (ms)\tHuffman Comp. Rate\tHuffman Comp. Time (ms)\tHuffman Decomp. Time (ms)"
        )

        for (entry in sortedSummary) {
            summaryReport.appendLine(
                "${entry.fileSize}\t" +
                        "${entry.lzwCompressionRate / 100}\t" +
                        "${entry.lzwCompressionTime}\t" +
                        "${entry.lzwDecompressionTime}\t" +
                        "${entry.huffmanCompressionRate / 100}\t" +
                        "${entry.huffmanCompressionTime}\t" +
                        "${entry.huffmanDecompressionTime}"
            )
        }
        println(summaryReport.toString())
    }


    private fun formatSize(sizeInBytes: Int): String {
        val size = sizeInBytes.toFloat()
        return when {
            size >= 1_048_576 -> String.format("%.2f MB", size / 1_048_576)
            size >= 1_024 -> String.format("%.2f KB", size / 1_024)
            else -> String.format("%.2f bytes", size)
        }
    }

    private fun formatRatio(ratio: Double): String = "%.2f%%".format(ratio)

    private fun formatTime(timeMs: Long): String = "%.2f ms".format(timeMs.toDouble())

    private fun calculateLzwDictSize(compressedData: List<Int>): Int {
        return (compressedData.maxOrNull()?.plus(1)) ?: 0
    }

    private fun isLossless(original: String, decompressed: String): Boolean {
        return original == decompressed
    }
}