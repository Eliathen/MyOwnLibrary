package com.szymanski.myownlibrary.data.services

import com.szymanski.myownlibrary.data.models.BookResult
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("books?jscmd=data&format=json")
    suspend fun getBookByIsbn(
        @Query("bibkeys") isbn: String
    ): BookResult
}