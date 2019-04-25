package id.jasoet.gson

import com.google.gson.JsonArray
import com.jayway.jsonpath.TypeRef
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

data class Book(val title: String, val category: String, val author: String, val isbn: String? = null, val price: Double)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonQueryTest {
    val json = Json()

    @Test
    fun `should able to from valid JsonObject String`() {
        val element = loadResource("/Goessner.json")
        val queryResult = json.query<JsonArray>(element, "$.store.book[*].author")
        queryResult.shouldNotBeEmpty()

        val queryResultList: List<String> = json.queryType(element, "$.store.book[*].author")
        queryResultList.shouldNotBeEmpty()

        val queryResultString = json.queryString(element, "$.store.book[*]")
        queryResultString.shouldNotBeEmpty()

        val typeRef = object : TypeRef<List<Book>>() {}

        val queryResultListObject: List<Book> = json.queryList(element, "$.store.book[*]", typeRef)
        queryResultListObject.forEach {
            println(it)
        }
        queryResultListObject.shouldNotBeEmpty()
    }
}
