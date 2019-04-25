package id.jasoet.gson

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL

private val jsonParser = JsonParser()


fun load(): JsonElement {
    return load(System.`in`)
}

fun load(json: String): JsonElement {
    return jsonParser.parse(json)
}

fun load(file: File): JsonElement {
    return load(FileInputStream(file))
}

fun load(inputStream: InputStream): JsonElement {
    return jsonParser.parse(loadString(inputStream))
}

fun loadResource(name: String): JsonElement {
    return load(jsonParser.javaClass.getResourceAsStream(name))
}

fun loadRemote(url: URL): JsonElement {
    val jsonString = url.readText()
    return jsonParser.parse(jsonString)
}

fun loadRemote(url: String): JsonElement {
    return loadRemote(URL(url))
}
