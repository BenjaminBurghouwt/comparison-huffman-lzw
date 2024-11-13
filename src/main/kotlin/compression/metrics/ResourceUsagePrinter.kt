package com.bburghouwt.compression.metrics

class ResourceUsagePrinter {

    private val metrics = CompressionMetrics()

    private val resourceUsageData = mutableListOf<ResourceUsageData>()

    fun appendResourceUsage(
        inputText: String,
        lzwUsage: Pair<Long, Double>,
        huffmanUsage: Pair<Long, Double>
    ) {
        println("\u001B[32mCompressed file: ${metrics.formatSize(inputText.length)}\u001B[0m")
        resourceUsageData.add(
            ResourceUsageData(
                inputText,
                lzwUsage.first,
                lzwUsage.second,
                huffmanUsage.first,
                huffmanUsage.second
            )
        )
    }

    fun printResourceUsageTable() {
        val sortedResourceUsageData = resourceUsageData.sortedBy { it.inputText.length }

        println("=== Resource Usage Table ===")
        println(String.format("%-15s | %-15s | %-15s | %-15s | %-15s", "File Size", "LZW Memory", "LZW CPU (%)", "Huffman Memory", "Huffman CPU (%)"))
        println("-".repeat(80))

        sortedResourceUsageData.forEach { usage ->
            val fileSize = metrics.formatSize(usage.inputText.length)
            println(
                String.format("%-15s | %-15s | %-15.2f | %-15s | %-15.2f",
                fileSize,
                metrics.formatSize(usage.lzwMemoryUsage.toInt()),
                    usage.lzwCpuUsage,
                    metrics.formatSize(usage.huffmanMemoryUsage.toInt()),
                    usage.huffmanCpuUsage)
            )
        }
    }
}