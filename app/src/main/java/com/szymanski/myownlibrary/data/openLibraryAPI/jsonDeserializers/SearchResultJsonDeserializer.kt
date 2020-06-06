package com.szymanski.myownlibrary.data.openLibraryAPI.jsonDeserializers

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.szymanski.myownlibrary.data.openLibraryAPI.models.Author
import com.szymanski.myownlibrary.data.openLibraryAPI.models.SearchBook
import com.szymanski.myownlibrary.data.openLibraryAPI.models.SearchResult
import java.lang.reflect.Type

class SearchResultJsonDeserializer: JsonDeserializer<SearchResult> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): SearchResult {
        val jsonArray = json?.asJsonObject?.get("docs")?.asJsonArray

        val searchResult = SearchResult()
        if (jsonArray != null) {
            for(it in jsonArray){
                if(!it.asJsonObject.has("isbn")){
                    continue
                }
                val isbn = getIsbnIfExist(it.asJsonObject, "isbn")
                val book = SearchBook(
                    getStringValueIfExist(it.asJsonObject, "title"),
                    getCoverUrlIfExist(isbn),
                    getAuthorsIfExist(it.asJsonObject, "author_name"),
                    getStringValueIfExist(it.asJsonObject, "first_publish_year"),
                    isbn,
                    0
                    )
                searchResult.searchBooks.add(book)
            }
        }
        return searchResult
    }

    private fun getStringValueIfExist(json: JsonObject, key: String): String{
        if(json.has(key)){
            return json.get(key).asString
        }
        return ""
    }
    private fun getCoverUrlIfExist(isbn: String): String{
            return "https://covers.openlibrary.org/b/isbn/$isbn-M.jpg"
    }
    private fun getAuthorsIfExist(jsonObject: JsonObject, key: String): MutableList<String>{
        val authors = mutableListOf<String>()
        if(jsonObject.has(key)){
            var author = ""
            jsonObject.getAsJsonArray(key).forEach {
                author = it.asString
            }
            authors.add(author)
        }
        return authors
    }
    private fun getIsbnIfExist(json: JsonObject, key: String): String{
        if(json.has(key)){
            return json.getAsJsonArray(key).first().asString
        }
        return ""
    }
    private fun getPagesIfExist(json: JsonObject, key: String): Int{
        if(json.has(key)){
            return json.get(key).asInt
        }
        return 0
    }
}