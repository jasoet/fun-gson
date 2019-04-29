package id.jasoet.gson

import com.google.gson.JsonElement
import com.sun.net.httpserver.HttpServer
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldNotBeBlank
import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.FileOutputStream
import java.net.InetSocketAddress

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoaderTest {

    private fun verifyValidJson(element: JsonElement) {
        element.isJsonObject.shouldBeTrue()

        val glossary = element.asJsonObject["glossary"].asJsonObject
        glossary.has("title").shouldBeTrue()
        glossary["title"].isJsonPrimitive

        val glossDiv = glossary["GlossDiv"].asJsonObject
        glossDiv.has("title").shouldBeTrue()

        val glossEntry = glossDiv.getAsJsonObject("GlossList").getAsJsonObject("GlossEntry")
        glossEntry.has("GlossDef").shouldBeTrue()

        val empty = glossEntry.get("Empty")
        empty.isJsonNull.shouldBeTrue()

        val glossSeeAlso = glossEntry.getAsJsonObject("GlossDef").get("GlossSeeAlso")
        glossSeeAlso.isJsonArray.shouldBeTrue()

    }

    @Nested
    inner class RemoteLoadTest {
        private lateinit var server: HttpServer

        fun before() {
            server = HttpServer.create(InetSocketAddress(5665), 0)
            server.createContext("/json") {
                val result = IOUtils.toString(javaClass.getResourceAsStream("/Valid.json"), Charsets.UTF_8)
                it.sendResponseHeaders(200, result.length.toLong())
                it.responseBody.use { os ->
                    os.write(result.toByteArray())
                }
            }
            server.start()
        }


        @Test
        fun `should able to from valid JsonObject Result`() {
            before()

            val jsonElement = loadRemote("http://localhost:5665/json")
            verifyValidJson(jsonElement)

            val jsonElementString = loadRemoteAsString("http://localhost:5665/json")
            verifyValidJson(load(jsonElementString))

            after()
        }

        fun after() {
            server.stop(1)
        }
    }

    @Nested
    inner class LoadStringTest {


        @Test
        fun `should able to from valid JsonObject InputStream`() {
            val input = javaClass.getResourceAsStream("/Valid.json")
            val jsonElement = loadString(input)
            verifyValidJson(load(jsonElement))
        }

        @Test
        fun `should able to from valid JsonObject File`() {
            val input = javaClass.getResourceAsStream("/Valid.json")
            val tempFile = createTempFile()
            IOUtils.copy(input, FileOutputStream(tempFile))

            val jsonElement = loadString(tempFile)
            verifyValidJson(load(jsonElement))
        }

        @Test
        fun `should able to from valid JsonObject StdIn`() {
            val input = javaClass.getResourceAsStream("/Valid.json")
            System.setIn(input)

            val jsonElement = loadString()
            verifyValidJson(load(jsonElement))
        }
    }

    @Nested
    inner class LocalLoadTest {
        @Test
        fun `should able to from valid JsonObject String`() {
            val input = IOUtils.toString(javaClass.getResourceAsStream("/Valid.json"), Charsets.UTF_8)
            input.shouldNotBeBlank()

            val jsonElement = load(input)
            verifyValidJson(jsonElement)
        }

        @Test
        fun `should able to from valid JsonObject Resource`() {
            val jsonElement = loadResource("/Valid.json")
            verifyValidJson(jsonElement)
        }

        @Test
        fun `should able to from valid JsonObject InputStream`() {
            val input = javaClass.getResourceAsStream("/Valid.json")
            val jsonElement = load(input)
            verifyValidJson(jsonElement)
        }

        @Test
        fun `should able to from valid JsonObject File`() {
            val input = javaClass.getResourceAsStream("/Valid.json")
            val tempFile = createTempFile()
            IOUtils.copy(input, FileOutputStream(tempFile))

            val jsonElement = load(tempFile)
            verifyValidJson(jsonElement)
        }

        @Test
        fun `should able to from valid JsonObject StdIn`() {
            val input = javaClass.getResourceAsStream("/Valid.json")
            System.setIn(input)

            val jsonElement = load()
            verifyValidJson(jsonElement)
        }
    }
}
