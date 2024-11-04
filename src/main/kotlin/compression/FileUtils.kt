package com.bburghouwt.compression

import java.nio.file.Files
import java.nio.file.Paths

fun getTextFiles(directory: String): List<String> {
    val textFiles = mutableListOf<String>()
    Files.newDirectoryStream(Paths.get(directory), "*.txt").use { stream ->
        for (path in stream) {
            textFiles.add(path.toString())
        }
    }
    return textFiles
}

fun readTextFile(filePath: String): String {
    return String(Files.readAllBytes(Paths.get(filePath)))
}