package com.szymanski.myownlibrary.data.openLibraryAPI.repositories

import com.szymanski.myownlibrary.data.openLibraryAPI.RetrofitBuilder
import com.szymanski.myownlibrary.data.openLibraryAPI.models.SearchResult

object SearchRepository {
    suspend fun findBooksByKeyword(keyword: String): SearchResult {
        return RetrofitBuilder.searchService.findBooksByKeyword(keyword)
    }
}