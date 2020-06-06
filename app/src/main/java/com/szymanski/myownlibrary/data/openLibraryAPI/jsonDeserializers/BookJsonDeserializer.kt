package com.szymanski.myownlibrary.data.openLibraryAPI.jsonDeserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book
import com.szymanski.myownlibrary.data.openLibraryAPI.models.BookInfo
import com.szymanski.myownlibrary.data.openLibraryAPI.models.BookResult
import java.lang.reflect.Type

class BookJsonDeserializer: JsonDeserializer<BookResult> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BookResult {
        val bookInfo = json?.asJsonObject?.get(json.asJsonObject.keySet().first().toString())?.asJsonObject

        val authors = arrayListOf<String>()
        bookInfo?.get("authors")?.asJsonArray?.forEach {
            authors.add(it.asJsonObject.get("name").toString().substring(1 until it.asJsonObject.get("name").toString().length - 1))
        }

        return BookResult(
            BookInfo(
                Book(
                    json?.asJsonObject?.keySet()?.first().toString().removePrefix("isbn:"),
                    ifNotNullGetStringValue(bookInfo,"title"),
                    authors,
                    extractYearFromData(ifNotNullGetStringValue(bookInfo,"publish_date")),
                    ifNotNullGetIntValue(bookInfo, "number_of_pages"),
                    if(bookInfo?.has("cover")!!) ifNotNullGetStringValue(bookInfo.get("cover")?.asJsonObject, "medium") else ""
                )
            )
        )
    }
    private fun ifNotNullGetStringValue(json: JsonObject?, key: String): String{
        if(json?.has(key)!!){
            return removeQuotes(json.get(key).toString())
        }
        return ""
    }
    private fun ifNotNullGetIntValue(json: JsonObject?, key: String): Int{
        if(json?.has(key)!!){
            return json.get(key).asInt
        }
        return 0
    }
    private fun removeQuotes(phrase: String): String{
        return phrase.substring(1 until phrase.length - 1)
    }
    private fun extractYearFromData(data: String): String{
        return if(data.length > 4){
            data.split(" ").last()
        } else {
            data
        }
    }

}