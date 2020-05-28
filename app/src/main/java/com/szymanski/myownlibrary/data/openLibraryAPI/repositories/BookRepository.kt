package com.szymanski.myownlibrary.data.openLibraryAPI.repositories

import com.szymanski.myownlibrary.data.openLibraryAPI.RetrofitBuilder
import com.szymanski.myownlibrary.data.openLibraryAPI.models.BookResult


object BookRepository {

    suspend fun getBookByIsbn(isbn: String): BookResult {
        return RetrofitBuilder.bookService.getBookByIsbn(isbn)
    }
}