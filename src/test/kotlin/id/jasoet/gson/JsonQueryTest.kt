package id.jasoet.gson

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.jayway.jsonpath.Criteria.where
import com.jayway.jsonpath.Filter.filter
import com.jayway.jsonpath.Option
import com.jayway.jsonpath.Predicate
import com.jayway.jsonpath.TypeRef
import org.amshove.kluent.shouldNotBeEmpty
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.lang.reflect.Type
import java.util.Date

data class Book(val title: String, val category: String, val author: String, val isbn: String? = null, val price: Double)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonQueryTest {

    @Test
    fun `should able to from valid JsonObject String`() {
        val json = Json(options = setOf(Option.AS_PATH_LIST))

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

    @Test
    fun `should able to from valid JsonObject with Inline Predicate`() {
        val json = Json()
        val element = loadResource("/Goessner.json")

        val typeRef = object : TypeRef<List<Book>>() {}
        val queryResultListObject: List<Book> = json.queryType(element, "$.store.book[?(@.price < 10)]", typeRef)
        queryResultListObject.forEach {
            println(it)
        }
        queryResultListObject.shouldNotBeEmpty()
    }

    @Test
    fun `should able to from valid JsonObject with Filter Predicate`() {
        val json = Json()
        val element = loadResource("/Goessner.json")

        val typeRef = object : TypeRef<List<Book>>() {}

        val filter = filter(
                where("category").`is`("fiction").and("price").lte(10.0)
        )

        val queryResultListObject: List<Book> = json.queryType(element, "$.store.book[?(@.price < 10)]", typeRef, filter)
        queryResultListObject.forEach {
            println(it)
        }
        queryResultListObject.shouldNotBeEmpty()
    }


    @Test
    fun `should able to from valid JsonObject with TypeRef`() {
        val json = Json()
        val element = loadResource("/Goessner.json")

        val typeRef = object : TypeRef<List<Book>>() {}
        val queryResultListObject: List<Book> = json.queryType(element, "$.store.book[*]", typeRef)
        queryResultListObject.forEach {
            println(it)
        }
        queryResultListObject.shouldNotBeEmpty()
    }

    @Test
    fun `should able to from valid JsonObject with querySet`() {
        val json = Json()
        val element = loadResource("/Goessner.json")

        val typeRef = object : TypeRef<Set<Book>>() {}
        val queryResultListObject: Set<Book> = json.querySet(element, "$.store.book[*]", typeRef)
        queryResultListObject.forEach {
            println(it)
        }
        queryResultListObject.shouldNotBeEmpty()
    }

    @Test
    fun `should able to from valid JsonObject with queryMap`() {
        val json = Json()
        val element = loadResource("/Goessner.json")

        val typeRef = object : TypeRef<Map<String, JsonArray>>() {}
        val queryResultListObject: Map<String, JsonArray> = json.queryMap(element, "$.store", typeRef)
        queryResultListObject.forEach {
            println(it)
        }
        queryResultListObject.shouldNotBeEmpty()
    }

    @Test
    fun `test simple mapping`() {
        val jsonElement = load("{\"date_as_long\" : 1411455611975}")
        val json = Json(config = {
            GsonBuilder()
                    .registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
                        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
                            return Date(json.asLong)
                        }
                    })
                    .create()
        })

        val date = json.queryType<Date>(jsonElement, "$['date_as_long']")
        date.shouldNotBeNull()
    }
}
