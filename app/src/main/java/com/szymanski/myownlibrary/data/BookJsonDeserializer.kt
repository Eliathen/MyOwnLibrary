package com.szymanski.myownlibrary.data

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.szymanski.myownlibrary.data.models.Book
import com.szymanski.myownlibrary.data.models.BookInfo
import com.szymanski.myownlibrary.data.models.BookResult
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
                    ifNotNullGetStringValue(bookInfo,"title"),
                    authors,
                    ifNotNullGetStringValue(bookInfo,"publish_date"),
                    ifNotNullGetIntValue(bookInfo, "number_of_pages"),
                    ifNotNullGetStringValue(bookInfo?.get("cover")?.asJsonObject, "medium")
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
}