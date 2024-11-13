package com.bburghouwt.compression.metrics

import java.lang.management.ManagementFactory
import com.sun.management.OperatingSystemMXBean

private const val BYTE_LENGTH = 8
private const val BYTE_TO_KB = 1024
private const val BYTE_TO_MB = 1_048_576

class CompressionMetrics {

    fun calculateCompressionRate(originalSizeBits: Int, compressedSizeBits: Int): Double {
        return (compressedSizeBits / originalSizeBits.toDouble()) * 100
    }

    fun huffmanCompressionRate(input: String, compressedData: String): Double {
        val originalSizeBits = input.length * BYTE_LENGTH  // ASCII encoding
        val compressedSizeBits = compressedData.length
        return calculateCompressionRate(originalSizeBits, compressedSizeBits)
    }

    fun lzwCompressionRate(input: String, compressedData: List<Int>, bitWidth: Int): Double {
        val originalSizeBits = input.length * BYTE_LENGTH // ASCII encoding
        val compressedSizeBits = compressedData.size * bitWidth
        return calculateCompressionRate(originalSizeBits, compressedSizeBits)
    }

    fun formatSize(sizeInBytes: Int): String {
        val absSize = kotlin.math.abs(sizeInBytes).toFloat()
        val formattedSize = when {
            absSize >= BYTE_TO_MB -> String.format("%.2f MB", absSize / BYTE_TO_MB)
            absSize >= BYTE_TO_KB -> String.format("%.2f KB", absSize / BYTE_TO_KB)
            else -> String.format("%.2f bytes", absSize)
        }
        return if (sizeInBytes < 0) "-$formattedSize" else formattedSize
    }

    fun formatRatio(ratio: Double): String = "%.2f%%".format(ratio)

    fun formatTime(timeMs: Long): String = "%.2f ms".format(timeMs.toDouble())

    fun calculateLzwDictSize(compressedData: List<Int>): Int {
        return (compressedData.maxOrNull()?.plus(1)) ?: 0
    }

    fun isLossless(original: String, decompressed: String): Boolean {
        return original == decompressed
    }

    fun measureResourceUsage(block: () -> Unit): Pair<Long, Double> {
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

    inline fun <T> measureTimeMillis(block: () -> T): TimedResult<T> {
        val start = System.currentTimeMillis()
        val result = block()
        val end = System.currentTimeMillis()
        return TimedResult(result, end - start)
    }

    data class TimedResult<T>(val result: T, val time: Long)
}