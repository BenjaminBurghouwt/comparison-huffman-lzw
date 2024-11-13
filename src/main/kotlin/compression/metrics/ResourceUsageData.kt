package com.bburghouwt.compression.metrics

data class ResourceUsageData(
    val inputText: String,
    val lzwMemoryUsage: Long,
    val lzwCpuUsage: Double,
    val huffmanMemoryUsage: Long,
    val huffmanCpuUsage: Double
)