package com.bburghouwt.compression.utils

import java.io.File
import kotlin.random.Random

const val SIZE_IN_MB = 1
val PRINTABLE_ASCII_RANGE = 65..122


fun main() {
    generateAsciiFile(SIZE_IN_MB)
    
//    val filePath = "src/main/resources/texts/40MB.txt"
//    val newFilePath = "src/main/resources/texts/80MB.txt"
//
//    shuffleAndDuplicateFileContent(filePath, newFilePath)

}

fun shuffleAndDuplicateFileContent(originalFilePath: String, newFilePath: String) {
    val originalFile = File(originalFilePath)
    if (!originalFile.exists()) {
        println("Error: File does not exist at $originalFilePath")
        return
    }

    val content = originalFile.readText()
    val chunkSize = content.length / 100
    val chunks = content.chunked(chunkSize).toMutableList()

    val shuffledChunks = chunks.shuffled(Random)
    val otherShuffledChucks = chunks.shuffled(Random)
    val newContent = (shuffledChunks + otherShuffledChucks).joinToString("\n")

    val newFile = File(newFilePath)
    newFile.writeText(newContent)
}

fun generateAsciiFile(sizeInMB: Int) {
    val filePath = "src/main/resources/shelf/Random${sizeInMB}MB.txt"
    val file = File(filePath)

    if (file.exists()) {
        println("\u001B[31mError: File already exists at $filePath\u001B[0m")
        return
    }

    val bytesPerMB = 1_048_576
    val totalBytes = sizeInMB * bytesPerMB

    file.bufferedWriter().use { writer ->
        repeat(totalBytes) {
            val randomChar = when (Random.nextInt(100)) {
                in 0..9 -> " "
                in 10..11 -> "\n"
                else -> Random.nextInt(PRINTABLE_ASCII_RANGE.first, PRINTABLE_ASCII_RANGE.last).toChar()
            }

            writer.write(randomChar.toString())
        }
    }

    println("\u001B[32mFile generated at $filePath with size ${sizeInMB}MB\u001B[0m")
}