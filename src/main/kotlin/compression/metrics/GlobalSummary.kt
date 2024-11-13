package com.bburghouwt.compression.metrics

object GlobalSummary {

    private val metrics = CompressionMetrics()

    data class SummaryEntry(
        val fileSize: Int,
        val lzwCompressionRate: Double,
        val lzwCompressionTime: Long,
        val lzwDecompressionTime: Long,
        val huffmanCompressionRate: Double,
        val huffmanCompressionTime: Long,
        val huffmanDecompressionTime: Long,
    )

    fun printSummary(summary: List<SummaryEntry>) {
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
                    metrics.formatSize(entry.fileSize),
                    metrics.formatRatio(entry.lzwCompressionRate),
                    entry.lzwCompressionTime,
                    entry.lzwDecompressionTime,
                    metrics.formatRatio(entry.huffmanCompressionRate),
                    entry.huffmanCompressionTime,
                    entry.huffmanDecompressionTime
                )
            )
        }
        println(summaryReport.toString())
    }

    fun printSummaryForSheets(summary: List<SummaryEntry>) {
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
}