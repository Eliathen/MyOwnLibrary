package com.szymanski.myownlibrary.data.repositories

import com.szymanski.myownlibrary.data.RetrofitBuilder
import com.szymanski.myownlibrary.data.models.BookResult


object BookRepository {

    suspend fun getBookByIsbn(isbn: String): BookResult {
        return RetrofitBuilder.bookService.getBookByIsbn(isbn)
    }
}