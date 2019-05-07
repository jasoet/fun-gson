# Fun Gson

[![Build Status](https://travis-ci.org/jasoet/fun-gson.svg?branch=master)](https://travis-ci.org/jasoet/fun-gson)
[![codecov](https://codecov.io/gh/jasoet/fun-gson/branch/master/graph/badge.svg)](https://codecov.io/gh/jasoet/fun-gson)
[![Download](https://api.bintray.com/packages/jasoet/fun/fun-gson/images/download.svg)](https://bintray.com/jasoet/fun/fun-gson/_latestVersion)

This library wraps [Gson][1] and [JsonPath][2] library. This library provides helper to load, parse and query the JSON document.

## Add Maven Central or JCenter Repository
```kotlin
repositories {
    jcenter()
}

```

## Gradle
```kotlin
implementation("id.jasoet:fun-gson:<version>")
```

## Maven
```xml
<dependency>
  <groupId>id.jasoet</groupId>
  <artifactId>fun-gson</artifactId>
  <version>[VERSION]</version>
  <type>pom</type>
</dependency>
```


## Load and Parse Document 
This library able to load Json Document from `File`, `InputStream`, ClassPath Resources, and Standard Input (`System.in`) then convert it to Gson `JsonElement` object.
It also provides feature to fetch Json Document from `URL` but it only use simple `URL.readText()` function, you can use more powerful HttpClient library.
See [LoaderTest](src/test/kotlin/id/jasoet/gson/LoaderTest.kt) for example.

## Query The Document
This library use [JsonPath](#jsonpath-link) to query the Json Document, so all of the features are supported. JsonPath support several JsonProvider and MappingProvider, in this library both Provider is mapped to Gson.
The following readme is taken from JsonPath Readme with some modification. 

## JsonPath     
JsonPath expressions always refer to a JSON structure in the same way as XPath expression are used in combination 
with an XML document. The "root member object" in JsonPath is always referred to as `$` regardless if it is an 
object or array.

JsonPath expressions can use the dot–notation

`$.store.book[0].title`

or the bracket–notation

`$['store']['book'][0]['title']`

### Operators

| Operator                  | Description                                                        |
| :------------------------ | :----------------------------------------------------------------- |
| `$`                       | The root element to query. This starts all path expressions.       |
| `@`                       | The current node being processed by a filter predicate.            |
| `*`                       | Wildcard. Available anywhere a name or numeric are required.       |
| `..`                      | Deep scan. Available anywhere a name is required.                  |
| `.<name>`                 | Dot-notated child                                                  |
| `['<name>' (, '<name>')]` | Bracket-notated child or children                                  |
| `[<number> (, <number>)]` | Array index or indexes                                             |
| `[start:end]`             | Array slice operator                                               |
| `[?(<expression>)]`       | Filter expression. Expression must evaluate to a boolean value.    |


### Functions

Functions can be invoked at the tail end of a path - the input to a function is the output of the path expression.
The function output is dictated by the function itself.

| Function                  | Description                                                         | Output    |
| :------------------------ | :------------------------------------------------------------------ |-----------|
| min()                     | Provides the min value of an array of numbers                       | Double    |
| max()                     | Provides the max value of an array of numbers                       | Double    |
| avg()                     | Provides the average value of an array of numbers                   | Double    |
| stddev()                  | Provides the standard deviation value of an array of numbers        | Double    |
| length()                  | Provides the length of an array                                     | Integer   |


### Filter Operators

Filters are logical expressions used to filter arrays. A typical filter would be `[?(@.age > 18)]` where `@` represents the current item being processed. More complex filters can be created with logical operators `&&` and `||`. String literals must be enclosed by single or double quotes (`[?(@.color == 'blue')]` or `[?(@.color == "blue")]`).   

| Operator                 | Description                                                           |
| :----------------------- | :-------------------------------------------------------------------- |
| ==                       | left is equal to right (note that 1 is not equal to '1')              |
| !=                       | left is not equal to right                                            |
| <                        | left is less than right                                               |
| <=                       | left is less or equal to right                                        |
| >                        | left is greater than right                                            |
| >=                       | left is greater than or equal to right                                |
| =~                       | left matches regular expression  [?(@.name =~ /foo.*?/i)]             |
| in                       | left exists in right [?(@.size in ['S', 'M'])]                        |
| nin                      | left does not exists in right                                         |
| subsetof                 | left is a subset of right [?(@.sizes subsetof ['S', 'M', 'L'])]       |
| anyof                    | left has an intersection with right [?(@.sizes anyof ['M', 'L'])]     |
| noneof                   | left has no intersection with right [?(@.sizes noneof ['M', 'L'])]    |
| size                     | size of left (array or string) should match right                     |
| empty                    | left (array or string) should be empty                                |


### Path Examples

Given the json

```json
{
    "store": {
        "book": [
            {
                "category": "reference",
                "author": "Nigel Rees",
                "title": "Sayings of the Century",
                "price": 8.95
            },
            {
                "category": "fiction",
                "author": "Evelyn Waugh",
                "title": "Sword of Honour",
                "price": 12.99
            },
            {
                "category": "fiction",
                "author": "Herman Melville",
                "title": "Moby Dick",
                "isbn": "0-553-21311-3",
                "price": 8.99
            },
            {
                "category": "fiction",
                "author": "J. R. R. Tolkien",
                "title": "The Lord of the Rings",
                "isbn": "0-395-19395-8",
                "price": 22.99
            }
        ],
        "bicycle": {
            "color": "red",
            "price": 19.95
        }
    },
    "expensive": 10
}
```

| JsonPath (click link to try)| Result |
| :------- | :----- |
| <a href="http://jsonpath.herokuapp.com/?path=$.store.book[*].author" target="_blank">$.store.book[*].author</a>| The authors of all books     |
| <a href="http://jsonpath.herokuapp.com/?path=$..author" target="_blank">$..author</a>                   | All authors                         |
| <a href="http://jsonpath.herokuapp.com/?path=$.store.*" target="_blank">$.store.*</a>                  | All things, both books and bicycles  |
| <a href="http://jsonpath.herokuapp.com/?path=$.store..price" target="_blank">$.store..price</a>             | The price of everything         |
| <a href="http://jsonpath.herokuapp.com/?path=$..book[2]" target="_blank">$..book[2]</a>                 | The third book                      |
| <a href="http://jsonpath.herokuapp.com/?path=$..book[2]" target="_blank">$..book[-2]</a>                 | The second to last book            |
| <a href="http://jsonpath.herokuapp.com/?path=$..book[0,1]" target="_blank">$..book[0,1]</a>               | The first two books               |
| <a href="http://jsonpath.herokuapp.com/?path=$..book[:2]" target="_blank">$..book[:2]</a>                | All books from index 0 (inclusive) until index 2 (exclusive) |
| <a href="http://jsonpath.herokuapp.com/?path=$..book[1:2]" target="_blank">$..book[1:2]</a>                | All books from index 1 (inclusive) until index 2 (exclusive) |
| <a href="http://jsonpath.herokuapp.com/?path=$..book[-2:]" target="_blank">$..book[-2:]</a>                | Last two books                   |
| <a href="http://jsonpath.herokuapp.com/?path=$..book[2:]" target="_blank">$..book[2:]</a>                | Book number two from tail          |
| <a href="http://jsonpath.herokuapp.com/?path=$..book[?(@.isbn)]" target="_blank">$..book[?(@.isbn)]</a>          | All books with an ISBN number         |
| <a href="http://jsonpath.herokuapp.com/?path=$.store.book[?(@.price < 10)]" target="_blank">$.store.book[?(@.price < 10)]</a> | All books in store cheaper than 10  |
| <a href="http://jsonpath.herokuapp.com/?path=$..book[?(@.price <= $['expensive'])]" target="_blank">$..book[?(@.price <= $['expensive'])]</a> | All books in store that are not "expensive"  |
| <a href="http://jsonpath.herokuapp.com/?path=$..book[?(@.author =~ /.*REES/i)]" target="_blank">$..book[?(@.author =~ /.*REES/i)]</a> | All books matching regex (ignore case)  |
| <a href="http://jsonpath.herokuapp.com/?path=$..*" target="_blank">$..*</a>                        | Give me every thing   
| <a href="http://jsonpath.herokuapp.com/?path=$..book.length()" target="_blank">$..book.length()</a>                 | The number of books                      |


## Reading a Document
Use `load(<parameter>)` function to read and parse a Document it will return JsonElement or use `loadAsString(<Paramete>)` to load and return String.
```kotlin
// Load from Url and Return JsonElement
val jsonElement = loadRemote("http://localhost:5665/json")
verifyValidJson(jsonElement)

// Load from Url and Return String
val jsonElementString = loadRemoteAsString("http://localhost:5665/json")
verifyValidJson(load(jsonElementString))

// Load from Classpath Resource
val jsonElement = loadResource("/Valid.json")
val input = javaClass.getResourceAsStream("/Valid.json")
            
// Load from File
val input = javaClass.getResourceAsStream("/Valid.json")
val tempFile = createTempFile()
IOUtils.copy(input, FileOutputStream(tempFile))
val jsonElement = load(tempFile)

// Load from InputStream
val input = javaClass.getResourceAsStream("/Valid.json")
val jsonElement = load(input)

```

## Query the Document
To Query the Document, we need `Json` instance. This instance allows us to configure the `GsonBuilder` and `JsonPath`.
```kotlin
// Create instance with default configuration
val json = Json()

// Configure Json
val json = Json(
        options = setOf(Option.ALWAYS_RETURN_LIST),
        config = {
            GsonBuilder().setDateFormat("dd-MM-yyyy").create()
        })
```

After configure the `Json` instance, now we can query the `JsonElement`.
```kotlin
// Return JsonArray 
val queryResult = json.query<JsonArray>(element, "$.store.book[*].author")

// Return List<String>
val queryResultList: List<String> = json.queryType(element, "$.store.book[*].author")

// Return String
val queryResultString = json.queryString(element, "$.store.book[*]")

// Return List of Object
data class Book(val title: String, val category: String, val author: String, val isbn: String? = null, val price: Double)
val typeRef = object : TypeRef<List<Book>>() {}
val queryResultListObject: List<Book> = json.queryList(element, "$.store.book[*]", typeRef)

```

When evaluating a path we need to understand the concept of when a path is `definite`. A path is `indefinite` if it contains:

* `..` - a deep scan operator
* `?(<expression>)` - an expression
* `[<number>, <number> (, <number>)]` - multiple array indexes

`Indefinite` paths always returns a list (as represented by Gson). 

Gson will Handle Mapping from Json to object, in the example below mapping between `Long` and `Date` is demonstrated. 

```kotlin
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

val date = json.queryType<Date>(jsonElement,"$['date_as_long']")
```

To obtaining full generics type information, use TypeRef.

```kotlin
val typeRef = object : TypeRef<List<Book>>() {}
val queryResultListObject: List<Book> = json.queryType(element, "$.store.book[*]", typeRef)
```

## Predicates
There are three different ways to create filter predicates in JsonPath.

### Inline Predicates
Inline predicates are the ones defined in the path.
```kotlin
val typeRef = object : TypeRef<List<Book>>() {}
val queryResultListObject: List<Book> = json.queryType(element, "$.store.book[?(@.price < 10)]", typeRef)
```

You can use `&&` and `||` to combine multiple predicates `[?(@.price < 10 && @.category == 'fiction')]` , 
`[?(@.category == 'reference' || @.price > 10)]`.
 
You can use `!` to negate a predicate `[?(!(@.price < 10 && @.category == 'fiction'))]`.

### Filter Predicates
Predicates can be built using the Filter API as shown below:
```kotlin
import com.jayway.jsonpath.Criteria.where
import com.jayway.jsonpath.Filter.filter

val typeRef = object : TypeRef<List<Book>>() {}

val filter = filter(
    where("category").`is`("fiction").and("price").lte(10.0)
)

val queryResultListObject: List<Book> = json.queryType(element, "$.store.book[?(@.price < 10)]", typeRef, filter)
```

Notice the placeholder `?` for the filter in the path. When multiple filters are provided they are applied in order where the number of placeholders must match 
the number of provided filters. You can specify multiple predicate placeholders in one filter operation `[?, ?]`, both predicates must match. 

Filters can also be combined with 'OR' and 'AND'
```kotlin
val filter = filter(
where("foo").exists(true)).or(where("bar").exists(true)
)

val filterFoo = filter(
where("foo").exists(true)).and(where("bar").exists(true)
)
```

### Roll Your Own
Third option is to implement your own predicates
 
```kotlin 
val booksWithIsbn = object:Predicate{
   override fun apply(ctx: Predicate.PredicateContext): Boolean {
       return ctx.item(Map::class.java).containsKey("isbn")
   }

}

val typeRef = object : TypeRef<List<Book>>() {}

val queryResultListObject: List<Book> = json.queryType(element, "$.store.book[?]", typeRef, booksWithIsbn)
```

### Path vs Value
In the Goessner implementation a JsonPath can return either `Path` or `Value`. `Value` is the default and what all the examples above are returning. If you rather have the path of the elements our query is hitting this can be achieved with an option.

```kotlin
val json = Json(options = setOf(Option.AS_PATH_LIST))

val pathList: List<String> = json.queryType(element, "$..author")

assertThat(pathList).containsExactly(
    "$['store']['book'][0]['author']",
    "$['store']['book'][1]['author']",
    "$['store']['book'][2]['author']",
    "$['store']['book'][3]['author']")
```

[1]: https://github.com/google/gson
[2]: https://github.com/json-path/JsonPath
