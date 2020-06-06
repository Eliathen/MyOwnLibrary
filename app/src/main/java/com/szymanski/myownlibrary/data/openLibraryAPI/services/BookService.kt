package com.szymanski.myownlibrary.data.openLibraryAPI.services

import com.szymanski.myownlibrary.data.openLibraryAPI.models.BookResult
import com.szymanski.myownlibrary.data.openLibraryAPI.models.SearchBook

import retrofit2.http.GET
import retrofit2.http.Query

interface BookService : SearchService {

    @GET("api/books?jscmd=data&format=json")
    suspend fun getBookByIsbn(
        @Query("bibkeys") isbn: String
    ): BookResult
}