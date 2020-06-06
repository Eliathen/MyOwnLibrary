package com.szymanski.myownlibrary.data.openLibraryAPI.services

import com.szymanski.myownlibrary.data.openLibraryAPI.models.SearchBook
import com.szymanski.myownlibrary.data.openLibraryAPI.models.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("search.json?")
    suspend fun findBooksByKeyword(
        @Query("q") keyword: String
    ): SearchResult
}