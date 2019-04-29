package id.jasoet.gson

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.Option
import com.jayway.jsonpath.ParseContext
import com.jayway.jsonpath.Predicate
import com.jayway.jsonpath.TypeRef
import com.jayway.jsonpath.spi.json.GsonJsonProvider
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider


class Json(options: Set<Option> = emptySet(), config: () -> Gson = { Gson() }) {
    private val gson = config()
    private val jsonProvider = GsonJsonProvider(gson)
    private val mappingProvider = GsonMappingProvider(gson)
    private val configuration = Configuration
            .builder()
            .jsonProvider(jsonProvider)
            .mappingProvider(mappingProvider)
            .options(options)
            .build()
    val parseContext: ParseContext = JsonPath.using(configuration)
}

fun <T : JsonElement> Json.query(json: JsonElement, path: String, vararg filters: Predicate): T {
    return this.parseContext.parse(json).read(path, *filters)
}

fun Json.queryString(json: JsonElement, path: String, vararg filters: Predicate): String {
    return query<JsonElement>(json, path, *filters).toString()
}

inline fun <reified T> Json.queryType(json: JsonElement, path: String, vararg filters: Predicate): T {
    return this.parseContext.parse(json).read(path, T::class.java, *filters)
}

fun <T> Json.queryType(
        json: JsonElement,
        path: String,
        typeRef: TypeRef<T>,
        vararg filters: Predicate
): T {
    val compiledPath = JsonPath.compile(path, *filters)
    return this.parseContext.parse(json).read(compiledPath, typeRef)
}

fun <T> Json.queryList(
        json: JsonElement,
        path: String,
        typeRef: TypeRef<List<T>>,
        vararg filters: Predicate
): List<T> {
    val compiledPath = JsonPath.compile(path, *filters)
    return this.parseContext.parse(json).read(compiledPath, typeRef)
}

fun <T> Json.querySet(
        json: JsonElement,
        path: String,
        typeRef: TypeRef<Set<T>>,
        vararg filters: Predicate
): Set<T> {
    val compiledPath = JsonPath.compile(path, *filters)
    return this.parseContext.parse(json).read(compiledPath, typeRef)
}

fun <T> Json.queryMap(
        json: JsonElement,
        path: String,
        typeRef: TypeRef<Map<String, T>>,
        vararg filters: Predicate
): Map<String, T> {
    val compiledPath = JsonPath.compile(path, *filters)
    return this.parseContext.parse(json).read(compiledPath, typeRef)
}

