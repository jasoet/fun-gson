package id.jasoet.gson

import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL
import java.nio.charset.StandardCharsets

private val charsets = StandardCharsets.UTF_8
fun loadString(inputStream: InputStream): String {
    return inputStream.use {
        IOUtils.toString(it, charsets)
    }
}

fun loadString(): String {
    return loadString(System.`in`)
}

fun loadString(file: File): String {
    return loadString(FileInputStream(file))
}

fun loadRemoteAsString(url: URL): String {
    return url.readText()
}

fun loadRemoteAsString(url: String): String {
    return loadRemoteAsString(URL(url))
}
